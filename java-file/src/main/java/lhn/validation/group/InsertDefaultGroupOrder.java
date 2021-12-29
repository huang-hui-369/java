package lhn.validation.group;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

@GroupSequence({Insert.class, Default.class})
public interface InsertDefaultGroupOrder {

}
