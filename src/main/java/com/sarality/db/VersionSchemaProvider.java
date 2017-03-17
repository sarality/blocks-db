package com.sarality.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * given a table definition and a schema update classifier, this class generates the table definition for each version
 *
 * @author Satya@ (Satya Puniani)
 */

class VersionSchemaProvider {

  private final TableDefinition definition;
  private final SchemaUpdateClassifier classifier;
  private final Integer currentVersion;
  private final Map<Integer, List<Column>> versionSchemas;

  private final Logger logger = LoggerFactory.getLogger(VersionSchemaProvider.class.getSimpleName());


  VersionSchemaProvider(TableDefinition tableDefinition, SchemaUpdateClassifier classifier, Integer currentVersion) {
    this.definition = tableDefinition;
    this.classifier = classifier;
    this.currentVersion = currentVersion;
    versionSchemas = new HashMap<>();
    GenerateVersionSchemas();

  }

  List<Column> getVersionSchema(Integer version) {
    return versionSchemas.get(version);
  }

  private void GenerateVersionSchemas() {

    versionSchemas.put(definition.getTableVersion(), Arrays.asList(definition.getColumns()));

    Map<String, Column> columnMap = new HashMap<>();
    for (Column column : definition.getColumns()) {
      columnMap.put(column.getName(), column);
    }

    for (Integer version = definition.getTableVersion(); version > currentVersion; version--) {

      logger.info("Rolling back operations for table {} in version {}.", definition.getTableName(), version);

      // is this a new table in this version?
      List<SchemaUpdate> updatesList = classifier.getVersionUpdatesForType(version, SchemaUpdateType.CREATE_TABLE);

      if (updatesList != null && updatesList.size() > 0) {
        //this is a new table, so downgrading would mean removing the table altogether
        columnMap.clear();
      }

      // are there any new columns added?
      updatesList = classifier.getVersionUpdatesForType(version, SchemaUpdateType.ADD_COLUMN);
      if (updatesList != null && updatesList.size() > 0) {
        for (SchemaUpdate schemaUpdate : updatesList) {
          columnMap.remove(((AddColumnSchemaUpdate) schemaUpdate).getNewColumn().getName());

        }
      }


      //After all updates are applied, we now have the schema for the rollback
      versionSchemas.put(version - 1, new ArrayList<>(columnMap.values()));

    }

  }

}