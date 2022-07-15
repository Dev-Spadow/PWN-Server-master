package com.arlania.world.content.digevent;

import com.arlania.model.Position;
import lombok.Getter;

public class DigSite {

    @Getter
    public Position pos;
    @Getter
    public int tokenQuantity;
    @Getter
    public boolean found;

    public DigSite(Position pos, int tokenQuantity, boolean found) {

        this.pos = pos;
        this.tokenQuantity = tokenQuantity;
        this.found = found;

    }
}
