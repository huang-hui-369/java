package lhn.validation.group;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;



/**
 * 设置验证顺序，先验证Insert Group， 再验证Default group
 *
 */
@GroupSequence({Insert.class, Default.class})
public interface InsertDefaultGroupOrder {

}
