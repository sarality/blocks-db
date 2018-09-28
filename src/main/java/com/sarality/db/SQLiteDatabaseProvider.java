package com.sarality.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Provides mechanism to open and close database table and run queries on them.
 *
 * @author abhideep@ (Abhideep Singh)
 */
class SQLiteDatabaseProvider extends SQLiteOpenHelper implements DatabaseProvider<SQLiteDatabase> {

  private final Logger logger = LoggerFactory.getLogger(SQLiteDatabaseProvider.class.getSimpleName());

  private final String dbName;
  private final int dbVersion;
  private List<TableDefinition> definitionList;

  private SQLiteDatabase database;

  SQLiteDatabaseProvider(Context context, String dbName, int dbVersion) {
    super(context, dbName, null, dbVersion);
    this.dbName = dbName;
    this.dbVersion = dbVersion;
  }

  @Override
  public void init(List<TableDefinition> definitionList) {
    this.definitionList = definitionList;
  }

  @Override
  public SQLiteDatabase getDatabase() {
    if (database == null) {
      database = getWritableDatabase();
    }
    return database;
  }

  @Override
  public void closeDatabase() {
    if (this.database != null && this.database.isOpen()) {
      this.database.close();
      this.database = null;
    }
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    logger.info("Creating tables for database {} version {}.", dbName, dbVersion);
    for (TableDefinition definition : definitionList) {
      logger.info("Creating tables " + definition.getTableName());
      database.execSQL(TableSQLGenerator.getCreateSql(definition.getTableName(), definition.getColumns()));

      // Create Indexes for the Table here
      SchemaUpdateClassifier classifier = new SchemaUpdateClassifier(definition);
      List<SchemaUpdate> updatesList = classifier.getVersionUpdatesForType(definition.getTableVersion(),
          SchemaUpdateType.ADD_INDEX);
      createIndexes(database, definition, updatesList);
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    logger.info("Upgrading tables for database {} to version {}.", dbName, dbVersion);

    for (TableDefinition definition : definitionList) {
      // get current schema
      List<Column> versionColumns = new ArrayList<>();
      SchemaUpdateClassifier classifier = new SchemaUpdateClassifier(definition);
      VersionSchemaProvider schemaProvider = new VersionSchemaProvider(definition, classifier, oldVersion);

      for (Integer version = oldVersion + 1; version <= newVersion; version++) {
        if (!classifier.isSchemaUpdatedInVersion(version)) {
          logger.info("No version upgrade operations for table {} in version {}.", definition.getTableName(), version);
          continue;
        }

        logger.info("Running upgrade operations for table {} in version {}.", definition.getTableName(), version);

        // Is this a new table in this version
        List<SchemaUpdate> updatesList = classifier.getVersionUpdatesForType(version, SchemaUpdateType.CREATE_TABLE);

        if (updatesList != null && updatesList.size() > 0) {
          // Create the table - sql
          logger.info("Create Table SQL: {} ", TableSQLGenerator.getCreateSql(definition.getTableName(),
              definition.getColumns()));

          db.execSQL(TableSQLGenerator.getCreateSql(definition.getTableName(),
              definition.getColumns()));

          // I can safely ignore all subsequent updates now
          break;
        }

        // Are there any new columns added?
        updatesList = classifier.getVersionUpdatesForType(version, SchemaUpdateType.ADD_COLUMN);
        if (updatesList != null && updatesList.size() > 0) {
          for (SchemaUpdate schemaUpdate : updatesList) {
            AddColumnSchemaUpdate addColumn = (AddColumnSchemaUpdate) schemaUpdate;
            logger.info("Add Column SQL: {} ",
                TableSQLGenerator.getAddColumnSql(definition.getTableName(),
                    addColumn.getNewColumn()));

            db.execSQL(
                TableSQLGenerator.getAddColumnSql(definition.getTableName(),
                    addColumn.getNewColumn()));
          }
        }

        // Are there any new Indexes that need to created
        updatesList = classifier.getVersionUpdatesForType(version, SchemaUpdateType.ADD_INDEX);
        createIndexes(db, definition, updatesList);
      } // For version loop
    }
  }


  private void createIndexes(SQLiteDatabase db, TableDefinition definition, List<SchemaUpdate> updatesList) {
    if (updatesList != null && updatesList.size() > 0) {
      for (SchemaUpdate schemaUpdate : updatesList) {
        AddIndexSchemaUpdate addIndex = (AddIndexSchemaUpdate) schemaUpdate;
        String updateSql = TableSQLGenerator.getCreateIndexSql(definition.getTableName(),
            addIndex.getIndexName(), addIndex.getColumns());
        logger.info("Add Index SQL: {} ", updateSql);
        db.execSQL(updateSql);
      }
    }
  }
}
