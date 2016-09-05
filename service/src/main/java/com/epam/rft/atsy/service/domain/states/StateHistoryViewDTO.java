package com.epam.rft.atsy.service.domain.states;

import com.epam.rft.atsy.service.domain.states.builder.AbstractStateBuilder;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * Supplies state history information to the view layer. Contains the time when the represented
 * state was created.
 * See {@link AbstractStateHistoryDTO AbstractStateHistoryDTO}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StateHistoryViewDTO extends AbstractStateHistoryDTO {


  private Date creationDate;


  public static StateViewDTOBuilder builder() {
    return new StateViewDTOBuilder();
  }

  public StateViewDTOBuilder construct() {
    return new StateViewDTOBuilder(this);
  }


  public static class StateViewDTOBuilder
      extends AbstractStateBuilder<StateViewDTOBuilder, StateHistoryViewDTO> {

    private StateViewDTOBuilder(StateHistoryViewDTO stateHistoryViewDTO) {
      super("StateViewDTOBuilder", stateHistoryViewDTO);
    }

    private StateViewDTOBuilder() {
      this(new StateHistoryViewDTO());
    }

    public StateViewDTOBuilder creationDate(Date creationDate) {
      object.setCreationDate(creationDate);
      return this;
    }

  }

}
