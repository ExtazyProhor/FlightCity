package com.mygdx.game.PlaneClasses;

public class Line {
    public Point point1;
    public Point point2;
    public boolean isVertical;

    public Line(Point a, Point b){
        this.point1 = a;
        this.point2 = b;
    }

    public float getK(){
        isVertical = (int)point1.x == (int)point2.x;
        if(isVertical) return 0;
        return (point1.y - point2.y) / (point1.x - point2.x);
    }

    public float getB(){
        return point1.y - getK() * point1.x;
    }

    public static boolean linesCollision(Line line1, Line line2){
        float k1 = line1.getK();
        float k2 = line2.getK();
        if(line1.isVertical && line2.isVertical) {
            return line1.point1.x == line2.point1.x;
        }
        else if(line1.isVertical){
            float collisionY = line2.getK() * line1.point1.x + line2.getB();
            float collisionX = (collisionY - line2.getB()) / line2.getK();
            return collisionY >= Math.min(line1.point1.y, line1.point2.y) && collisionY <= Math.max(line1.point1.y, line1.point2.y)
                    && collisionY >= Math.min(line2.point1.y, line2.point2.y) && collisionY <= Math.max(line2.point1.y, line2.point2.y)
                    && collisionX >= Math.min(line2.point1.x, line2.point2.x) && collisionX <= Math.max(line2.point1.x, line2.point2.x);
        } else if(line2.isVertical){
            float collisionY = line1.getK() * line2.point1.x + line1.getB();
            float collisionX = (collisionY - line1.getB()) / line1.getK();
            return collisionY >= Math.min(line1.point1.y, line1.point2.y) && collisionY <= Math.max(line1.point1.y, line1.point2.y)
                    && collisionY >= Math.min(line2.point1.y, line2.point2.y) && collisionY <= Math.max(line2.point1.y, line2.point2.y)
                    && collisionX >= Math.min(line1.point1.x, line1.point2.x) && collisionX <= Math.max(line1.point1.x, line1.point2.x);
        } else {
            if(k1 == k2) {
                return line1.getB() == line2.getB();
            }
            float collisionX = (line2.getB() - line1.getB()) / (line1.getK() - line2.getK());
            return collisionX >= Math.min(line1.point1.x, line1.point2.x) && collisionX <= Math.max(line1.point1.x, line1.point2.x)
                    && collisionX >= Math.min(line2.point1.x, line2.point2.x) && collisionX <= Math.max(line2.point1.x, line2.point2.x);
        }
    }
}