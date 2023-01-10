package org.example.actionselection;

import org.example.models.QModel;
import org.example.models.UtilityModel;
import org.example.utils.IndexValue;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @author furkangunes
 * Base strateji class'ı
 */
public interface ActionSelectionStrategy extends Serializable, Cloneable {
    IndexValue selectAction(int stateId, QModel model, Set<Integer> actionsAtState);

    IndexValue selectAction(int stateId, UtilityModel model, Set<Integer> actionsAtState);

    String getPrototype();

    Map<String, String> getAttributes();
}
