package model.world;

import java.awt.Point;

public interface Damageable {
public Point getLocation();
public int getCurrentHP();
public void setCurrentHP(int hp);
}
