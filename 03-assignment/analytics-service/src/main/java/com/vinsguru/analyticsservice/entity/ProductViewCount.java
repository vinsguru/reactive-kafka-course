package com.vinsguru.analyticsservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductViewCount implements Persistable<Integer> {

    @Id
    private Integer id;
    private Long count;

    @Transient
    private boolean isNew;

    @Override
    public boolean isNew() {
        return this.isNew || Objects.isNull(id);
    }
}
