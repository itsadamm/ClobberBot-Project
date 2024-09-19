import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.Vector;

public class adamadri extends ClobberBot {

    private int shotclock = 0;

    // Constructor
    public adamadri(Clobber game) {
        super(game);
    }

    @Override
    public String toString() {
        return "yourLoginName"; // Replace with your actual login name
    }

    @Override
    public void drawMe(Graphics g, int x, int y) {
        // Draw a simple representation of the bot
        g.fillOval(x - 10, y - 10, 20, 20); // 20x20 circle
    }

    @Override
    public ClobberBotAction takeTurn(WhatIKnow state) {
        // Get the bot's current position
        BotPoint2D myPosition = state.me;

        // Get the positions of other bots and bullets
        Vector<BulletPoint2D> bullets = state.bullets;
        Vector<BotPoint2D> bots = state.bots;

        // Avoid bullets
        if (!bullets.isEmpty()) {
            BulletPoint2D nearestBullet = bullets.get(0); // Example, find the nearest bullet
            Point2D bulletPosition = nearestBullet.getLocation();

            // Calculate avoidance logic here (e.g., move away from bullet)
            double bulletAngle = calculateAngle(myPosition.getLocation(), bulletPosition);
            return new ClobberBotAction(ClobberBotAction.Action.MOVE, bulletAngle + 90);
        }

        // Chase the closest bot
        if (!bots.isEmpty()) {
            BotPoint2D targetBot = bots.get(0); // Target the first bot
            Point2D targetPosition = targetBot.getLocation();

            // Calculate the angle to the target bot and move toward it
            double angleToTarget = calculateAngle(myPosition.getLocation(), targetPosition);
            return new ClobberBotAction(ClobberBotAction.Action.MOVE, angleToTarget);
        }

        // Shooting logic
        if (shotclock <= 0) {
            shotclock = 10; // Cooldown before firing again
            return new ClobberBotAction(ClobberBotAction.Action.FIRE, 0); // Fire in current direction
        }

        // Decrease shotclock each turn
        shotclock--;

        // Default action (random move if no immediate threat)
        return new ClobberBotAction(ClobberBotAction.Action.MOVE, rand.nextInt(360)); 
    }

    // Utility method to calculate angle between two points
    private double calculateAngle(Point2D from, Point2D to) {
        return Math.toDegrees(Math.atan2(to.getY() - from.getY(), to.getX() - from.getX()));
    }
}