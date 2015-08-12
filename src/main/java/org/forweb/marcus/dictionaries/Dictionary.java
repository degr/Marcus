package org.forweb.marcus.dictionaries;

import org.forweb.marcus.entity.Word;

/**
 * Created by rsmirnou on 7/31/2015. 47
 */
public interface Dictionary {
    public Word getWord(String name);

    public boolean saveWord(String string);

    boolean onNewWordRequest(String word);
}
