package com.widgets.management.api.domain.widget;

import lombok.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "widget")
@EnableAutoConfiguration
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Widget implements Serializable {

    private static final long serialVersionUID = 4686338443423283356L;

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @NotNull
    private Long id;

    @Column(name="widgetX")
    @NotNull
    private Long widgetX;

    @Column(name="widgetY")
    @NotNull
    private Long widgetY;

    @Column(name="widgetZIndex")
    @NotNull
    private Long widgetZIndex;

    @Column(name="width")
    @NotNull
    private Long width;

    @Column(name="height")
    @NotNull
    private Long height;

    @Column(name="lastModificationDate")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date lastModificationDate;

    public Widget(Long widgetX, Long widgetY, Long width, Long height)
    {
        this.widgetX = widgetX;
        this.widgetY = widgetY;
        this.width = width;
        this.height = height;
    }
}
