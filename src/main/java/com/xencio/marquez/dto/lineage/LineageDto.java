package com.xencio.marquez.dto.lineage;

import lombok.*;

import java.util.Date;
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
public class LineageDto {

    private String eventType;

    private Date eventTime;

    private Run run;

    private Job job;

    private List<Input> inputs;

    private List<Output> outputs;

    private String producer = "https://github.com/OpenLineage/OpenLineage/blob/v1-0-0/client";
}
