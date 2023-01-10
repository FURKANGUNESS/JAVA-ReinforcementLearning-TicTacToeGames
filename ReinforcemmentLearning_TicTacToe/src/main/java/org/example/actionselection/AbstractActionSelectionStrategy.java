package org.example.actionselection;

import org.example.models.QModel;
import org.example.models.UtilityModel;
import org.example.utils.IndexValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author furkangunes
 */
public abstract class AbstractActionSelectionStrategy implements ActionSelectionStrategy {

    private String prototype;
    protected Map<String, String> attributes = new HashMap<String, String>();

    public String getPrototype() {
        return prototype;
    }

    public IndexValue selectAction(int stateId, QModel model, Set<Integer> actionsAtState) {
        return new IndexValue();
    }

    public IndexValue selectAction(int stateId, UtilityModel model, Set<Integer> actionsAtState) {
        return new IndexValue();
    }

    public AbstractActionSelectionStrategy() {
        prototype = this.getClass().getCanonicalName();
    }


    public AbstractActionSelectionStrategy(HashMap<String, String> attributes) {
        this.attributes = attributes;
        if (attributes.containsKey("prototype")) {
            this.prototype = attributes.get("prototype");
        }
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     *
     * @param obj
     * @return boolean
     * Gelen obje ile eşitlik kontrolü
     */
    @Override
    public boolean equals(Object obj) {
        ActionSelectionStrategy rhs = (ActionSelectionStrategy) obj;
        if (!prototype.equalsIgnoreCase(rhs.getPrototype())) return false;
        for (Map.Entry<String, String> entry : rhs.getAttributes().entrySet()) {
            if (!attributes.containsKey(entry.getKey())) {
                return false;
            }
            if (!attributes.get(entry.getKey()).equals(entry.getValue())) {
                return false;
            }
        }
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            if (!rhs.getAttributes().containsKey(entry.getKey())) {
                return false;
            }
            if (!rhs.getAttributes().get(entry.getKey()).equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @return Object
     * Nesne kopyalanır
     */
    @Override
    public abstract Object clone();
}
