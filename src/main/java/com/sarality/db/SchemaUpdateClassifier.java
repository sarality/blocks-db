package com.sarality.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Takes a table definition and arranges the schema updates by version and update type
 *
 * @author Satya@ (Satya Puniani)
 */

class SchemaUpdateClassifier {

  private final String tableName;
  // A list of updates for each version
  private Map<Integer, Map<SchemaUpdateType, List<SchemaUpdate>>> versionUpdates = new HashMap<>();

  SchemaUpdateClassifier(TableDefinition tableDefinition) {
    tableName = tableDefinition.getTableName();
    List<SchemaUpdate> schemaUpdates = tableDefinition.getSchemaUpdates();
    SchemaSorter(schemaUpdates);

  }

  private void SchemaSorter(List<SchemaUpdate> schemaUpdateList) {

    for (SchemaUpdate schemaUpdate : schemaUpdateList) {
      Map<SchemaUpdateType, List<SchemaUpdate>> updatesMap = versionUpdates.get(schemaUpdate.getDbVersion());

      if (updatesMap == null) {
        updatesMap = new HashMap<>();
      }

      if (!updatesMap.containsKey(schemaUpdate.getUpdateType())) {
        updatesMap.put(schemaUpdate.getUpdateType(), new ArrayList<SchemaUpdate>());
      }

      updatesMap.get(schemaUpdate.getUpdateType()).add(schemaUpdate);
      versionUpdates.put(schemaUpdate.getDbVersion(), updatesMap);

    }

  }

  boolean isSchemaUpdatedInVersion(Integer dbVersion) {
    Map<SchemaUpdateType, List<SchemaUpdate>> updatesMap = versionUpdates.get(dbVersion);
    return updatesMap != null;
  }

  List<SchemaUpdate> getVersionUpdatesForType(Integer dbVersion, SchemaUpdateType updateType) {
    Map<SchemaUpdateType, List<SchemaUpdate>> updatesMap = versionUpdates.get(dbVersion);

    return updatesMap == null ? null : updatesMap.get(updateType);

  }

  String getTableName() {
    return this.tableName;
  }

}
