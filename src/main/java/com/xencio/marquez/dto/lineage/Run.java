package com.xencio.marquez.dto.lineage;

import lombok.*;

import java.util.UUID;

/**
 * @author songyr
 * @date 2022/06/23
 */
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Run {
    private String runId;

    public Run() {
        this.runId = UUID.randomUUID().toString();
    }
}
