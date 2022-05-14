package com.livetheoogway.teflon.framework.core;

import lombok.*;

import java.util.List;

/**
 * @author tushar.naik
 * @version 1.0  30/05/18 - 10:11 AM
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
@Builder
public class SourceInputs<Input, Progress> {
    private List<Input> inputs;
    private Progress progress;
}
