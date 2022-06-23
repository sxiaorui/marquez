package com.xencio.marquez.dto.lineage;

import lombok.*;

import java.util.List;

/**
 * @author songyr
 * @date 2022/06/23
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Schema {

    private String _producer = "https://github.com/OpenLineage/OpenLineage/blob/v1-0-0/client";

    private String _schemaURL = "https://github.com/OpenLineage/OpenLineage/blob/v1-0-0/spec/OpenLineage.json#/definitions/SchemaDatasetFacet";

    private List<Field> fields;
}
