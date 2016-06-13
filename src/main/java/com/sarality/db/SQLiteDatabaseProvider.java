package com.sarality.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Provides mechanism to open and close database table and run queries on them.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SQLiteDatabaseProvider extends SQLiteOpenHelper {

  private final TableDefinition definition;

  public SQLiteDatabaseProvider(Context context, TableDefinition definition) {
    super(context, definition.getDatabaseName(), null, definition.getTableVersion());
    this.definition = definition;
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(TableSQLGenerator.getCreateSql(definition.getTableName(), definition.getColumns()));
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL(TableSQLGenerator.getDropSql(definition.getTableName()));
    onCreate(db);
  }
}
