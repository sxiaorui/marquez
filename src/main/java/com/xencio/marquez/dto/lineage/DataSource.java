package com.xencio.marquez.dto.lineage;

import lombok.*;

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
public class DataSource {

    private String _producer = "https://github.com/OpenLineage/OpenLineage/blob/v1-0-0/client";

    private String _schemaURL = "https://github.com/OpenLineage/OpenLineage/blob/v1-0-0/spec/OpenLineage.json#/definitions/SchemaDatasetFacet";

    private String name;

    private String uri;
}
