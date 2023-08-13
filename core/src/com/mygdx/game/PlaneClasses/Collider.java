package com.mygdx.game.PlaneClasses;

import com.mygdx.game.RealClasses.Rectangle;

public class Collider extends Rectangle {
    public boolean isCollision(Collider anotherCollider) {
        if(this.y <= anotherCollider.y + anotherCollider.sizeY) return false;
        if(this.y + this.sizeY >= anotherCollider.y) return false;
        if(this.x <= anotherCollider.x + anotherCollider.sizeX) return false;
        return !(this.x + this.sizeX >= anotherCollider.x);
    }
}
