package com.spd.trello.domain.resources;

import com.spd.trello.domain.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "labels")
@Entity
public class Label extends Resource {
    private String colorName;
}
