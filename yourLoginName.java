import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.Vector;

public class yourLoginName extends ClobberBot {

    private int shotclock = 0; // Cooldown to prevent shooting too often
    private final int SAFE_DISTANCE = 50; // Minimum safe distance from bullets or other bots

    // Constructor
    public yourLoginName(Clobber game) {
        super(game);
    }

    @Override
    public String toString() {
        return "yourLoginName"; // Replace with your actual login name
    }

    @Override
    public void drawMe(Graphics g) {
        // Draw a simple representation of the bot
        g.fillOval((int) getPosition().getX() - 10, (int) getPosition().getY() - 10, 20, 20); // 20x20 circle
    }

    @Override
    public ClobberBotAction takeTurn(WhatIKnow state) {
        // Get the bot's current position
        BotPoint2D myPosition = state.me;

        // Get the positions of other bots and bullets
        Vector<BulletPoint2D> bullets = state.bullets;
        Vector<BotPoint2D> bots = state.bots;

        // Avoid bullets by moving away if they are within the safe distance
        BulletPoint2D nearestBullet = getNearestBullet(bullets, myPosition);
        if (nearestBullet != null && distanceTo(nearestBullet, myPosition) < SAFE_DISTANCE) {
            double bulletX = nearestBullet.getX();
            double bulletY = nearestBullet.getY();

            // Move perpendicular to the bullet trajectory to evade it
            double bulletAngle = calculateAngle(myPosition.getX(), myPosition.getY(), bulletX, bulletY);
            return new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHT); // MOVE to the right to avoid bullet
        }

        // Attack the nearest bot if it's close enough
        BotPoint2D targetBot = getNearestBot(bots, myPosition);
        if (targetBot != null && distanceTo(targetBot, myPosition) < SAFE_DISTANCE * 3) {
            double targetX = targetBot.getX();
            double targetY = targetBot.getY();

            // Calculate the angle to the target bot and move toward it
            double angleToTarget = calculateAngle(myPosition.getX(), myPosition.getY(), targetX, targetY);
            return new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.UP); // MOVE toward the target

            // If within shooting range, fire at the bot
            if (Math.abs(angleToTarget) < 10 && shotclock <= 0) {
                shotclock = 10; // Cooldown before firing again
                return new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.NONE); // SHOOT
            }
        }

        // Decrease shotclock each turn
        if (shotclock > 0) {
            shotclock--;
        }

        // Default action (move randomly if no immediate threat)
        return new ClobberBotAction(ClobberBotAction.MOVE, rand.nextInt(4) * 4); // MOVE randomly in a direction
    }

    // Utility method to calculate angle between two points
    private double calculateAngle(double x1, double y1, double x2, double y2) {
        return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
    }

    // Find the nearest bullet to the bot
    private BulletPoint2D getNearestBullet(Vector<BulletPoint2D> bullets, BotPoint2D myPosition) {
        BulletPoint2D nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (BulletPoint2D bullet : bullets) {
            double distance = distanceTo(bullet, myPosition);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = bullet;
            }
        }
        return nearest;
    }

    // Find the nearest bot to the player
    private BotPoint2D getNearestBot(Vector<BotPoint2D> bots, BotPoint2D myPosition) {
        BotPoint2D nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (BotPoint2D bot : bots) {
            double distance = distanceTo(bot, myPosition);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = bot;
            }
        }
        return nearest;
    }

    // Calculate the distance between two points
    private double distanceTo(Point2D p1, Point2D p2) {
        return p1.distance(p2);
    }
}