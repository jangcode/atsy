package com.epam.rft.atsy.service.domain;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * DTO for PositionEntity
 * See {@link com.epam.rft.atsy.persistence.entities.PositionEntity}.
 */
public class PositionDTO implements Serializable {

  private Long id;

  @NotNull
  @Size(min = 1)
  private String name;

}
