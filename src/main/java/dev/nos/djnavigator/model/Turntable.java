package dev.nos.djnavigator.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Turntable {

    @ManyToOne(fetch = FetchType.LAZY)
    private Track track;
    private BigDecimal pitch;
}
