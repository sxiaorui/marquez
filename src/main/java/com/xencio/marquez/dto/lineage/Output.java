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
public class Output {

    private String namespace;

    private String name;

    private Facets facets;
}
