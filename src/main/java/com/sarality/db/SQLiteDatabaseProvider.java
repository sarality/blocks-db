package com.sarality.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Provides mechanism to open and close database table and run queries on them.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SQLiteDatabaseProvider extends SQLiteOpenHelper implements DatabaseProvider<SQLiteDatabase> {

  private final Logger logger = LoggerFactory.getLogger(SQLiteDatabaseProvider.class.getSimpleName());

  private final String dbName;
  private final int dbVersion;
  private List<TableDefinition> definitionList;

  private SQLiteDatabase database;

  public SQLiteDatabaseProvider(Context context, String dbName, int dbVersion) {
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
  public void resetDatabase() {
    if (this.database != null) {
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
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    logger.info("Upgrading tables for database {} to version {}.", dbName, dbVersion);
    for (TableDefinition definition : definitionList) {
      logger.info("Dropping tables " + definition.getTableName());
      db.execSQL(TableSQLGenerator.getDropSql(definition.getTableName()));
    }
    onCreate(db);
  }

  @Override
  public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    logger.info("Downgrading tables for database {} to version {}.", dbName, dbVersion);
    for (TableDefinition definition : definitionList) {
      logger.info("Dropping tables " + definition.getTableName());
      db.execSQL(TableSQLGenerator.getDropSql(definition.getTableName()));
    }
    onCreate(db);
  }
}
