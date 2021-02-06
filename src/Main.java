package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Random;

//Dymashevskij Sergey
//Dymashevskij_Game/game
//17.03.2018

public class Main {
    static JScrollBar hard;
    static Timer timer;
    static int scr = 0;
    static boolean boss = false;
    static int lvl = 1;

    static class EnemyShip {
        ImageIcon img;
        double x;
        int y;
        int size;
        int health = new Random().nextInt(lvl * 2) + 2;
        int reload = 0;
        int maxhealth;

        public EnemyShip(int size, double x, int y) {
            img = new ImageIcon("enemyssh.png");
            this.size = size;
            this.size = 55;
            this.x = x;
            this.y = y;

            maxhealth = health;
        }

        EnemyShip() {
        }
    }

    static class Wall extends EnemyShip {
        public Wall(double x, int y) {
            super(50, x, y);
            health = new Random().nextInt(5 + lvl) + 3;
            maxhealth = health;
            size += new Random().nextInt(100);
        }
    }

    static class Boss extends EnemyShip {

        public Boss() {
            img = new ImageIcon("enemyssh.png");
            this.x = new Random().nextInt(700);
            this.y = 70;
            this.size = 100;

            health = lvl * 5 + 5;
            maxhealth = health;
        }
    }

    static class Meteor {
        ImageIcon img;
        double vx;
        double x;
        int y;
        double vy;
        int size;

        public Meteor(double vx, double vy, int size, double x, int y) {
            img = new ImageIcon("meteor.png");
            this.vx = vx;
            this.vy = vy;
            this.size = size;
            this.x = x;
            this.y = y;
        }
    }

    static class Bullet {
        ImageIcon img;
        double vx;
        double x;
        int y;
        double vy;
        int size;

        public Bullet(String path, double vx, double vy, int size, double x, int y) {
            img = new ImageIcon(path);
            this.vx = vx;
            this.vy = vy;
            this.size = size;
            this.x = x;
            this.y = y;
            this.size = 30;
        }
    }

    static class EnemyBullet extends Meteor {
        public EnemyBullet(double vx, double vy, int size, double x, int y) {
            super(vx, vy, size, x, y);
            this.img = new ImageIcon("enemyBullet.jpg");
            //this.size = 55;
        }
    }

    static class MyPanel extends JPanel {
        int mx = 100, my = getHeight() - 50;
        int laseralive = 0;
        int laserx = 0;
        int lasery = 0;
        int health = 5;
        double x = 100, y = getHeight() - 20;
        double vx = 0;
        double vy = 0;
        int reload = 0;
        boolean dd = false;
        int lasercount = 3;
        ArrayList<EnemyShip> enemies = new ArrayList<>();
        ArrayList<Bullet> bullets = new ArrayList<>();
        ArrayList<Meteor> meteors = new ArrayList<>();

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            super.paintComponent(g);
            ImageIcon img = new ImageIcon("spaceship.jpg");
            if (laseralive > 0) {
                g2d.setColor(new Color(100, 0, 0));
                g2d.fillRect(laserx, 0, 3, lasery);
                laseralive -= 1;
            }
            if (reload > 0) {
                reload--;
            }

            for (int i = 0; i < enemies.size(); i++) {
                for (int j = 0; j < bullets.size(); j++) {
                    if (i < enemies.size() && Math.abs(bullets.get(j).x - enemies.get(i).x - enemies.get(i).size / 2 + bullets.get(j).size / 2) <= enemies.get(i).size / 2 + bullets.get(j).size / 2 && Math.abs(bullets.get(j).y - enemies.get(i).y) <= 30) {
                        bullets.remove(j);
                        enemies.get(i).health -= 1;
                        j = 0;
                        i = 0;

                    }
                }
            }
            int i = 0;
            while (i < bullets.size()) {

                g2d.drawImage(bullets.get(i).img.getImage(), (int) bullets.get(i).x, bullets.get(i).y, 30, 50, new ImageObserver() {
                    @Override
                    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                        return false;
                    }
                });
                bullets.get(i).x += bullets.get(i).vx;
                bullets.get(i).y += bullets.get(i).vy;
                if (bullets.get(i).y < 0) {
                    bullets.remove(i);
                    i = 0;
                    continue;
                }
                i++;
            }
            for (i = 0; i < enemies.size(); ) {
                if (enemies.get(i).health <= 0) {
                    if (enemies.get(i).getClass() == Boss.class) {
                        Main.boss = false;
                        enemies = new ArrayList<>();
                        scr += 93 * lvl;
                        lasercount += 2;
                        lvl++;
                        break;
                    }
                    enemies.remove(i);
                    scr += 7;

                } else {
                    if (enemies.get(i).reload == 0 && enemies.get(i).getClass() != Wall.class) {
                        meteors.add(new EnemyBullet(((x - enemies.get(i).x) * 3 / (y - enemies.get(i).y)) + (-1 + new Random().nextInt(3)) / 2.0, 3, 30, enemies.get(i).x + enemies.get(i).size / 2 - 15, enemies.get(i).y + 10));
                        enemies.get(i).reload = 100;
                        if (enemies.get(i).getClass().equals(Boss.class)) {
                            enemies.get(i).reload = 25;
                        }
                    } else {
                        enemies.get(i).reload--;

                    }
                    i++;
                }
            }
            i = 0;

            for (; i < enemies.size(); i++) {
                int h = 0;
                for (int counter = 0; h < enemies.get(i).health; h++, counter += enemies.get(i).size / enemies.get(i).maxhealth) {
                    g2d.setColor(Color.green);
                    g2d.fillRect((int) enemies.get(i).x + counter, enemies.get(i).y - 5, enemies.get(i).size / enemies.get(i).maxhealth, 5);
                    g2d.setColor(Color.black);
                    g2d.drawRect((int) enemies.get(i).x + counter, enemies.get(i).y - 5, enemies.get(i).size / enemies.get(i).maxhealth, 5);
                    g2d.setColor(Color.white);
                }
                if (enemies.get(i).getClass() == EnemyShip.class || enemies.get(i).getClass() == Boss.class) {
                    g2d.drawImage(enemies.get(i).img.getImage(), (int) enemies.get(i).x, enemies.get(i).y, enemies.get(i).size, enemies.get(i).size, new ImageObserver() {
                        @Override
                        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                            return false;
                        }
                    });
                } else {
                    g2d.setColor(Color.black);
                    g2d.fillRect((int) enemies.get(i).x, enemies.get(i).y, enemies.get(i).size, 20);
                    g2d.setColor(Color.white);
                }
            }

            i = 0;
            while (i < meteors.size()) {
                g2d.drawImage(meteors.get(i).img.getImage(), (int) meteors.get(i).x, meteors.get(i).y, 30, 50, new ImageObserver() {
                    @Override
                    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                        return false;
                    }
                });
                if (meteors.get(i).x + meteors.get(i).vx > getWidth() || meteors.get(i).x + meteors.get(i).vx < 0) {
                    meteors.get(i).vx = -meteors.get(i).vx;
                }
                meteors.get(i).x += meteors.get(i).vx;
                meteors.get(i).y += meteors.get(i).vy;
                if (meteors.get(i).y > getHeight()) {
                    meteors.remove(i);
                    i = 0;
                    continue;
                }
                if (meteors.get(i).x + 30 >= x - 30 && meteors.get(i).x <= x + 30 && meteors.get(i).y + 50 >= y - 40 && meteors.get(i).y <= y + 40) {
                    health -= 1;
                    meteors.remove(i);
                    dd = false;
                    repaint();
                } else {
                    i++;
                }
            }
            i = 0;
            for (EnemyShip enemy : enemies) {
                if (enemy.getClass() == Wall.class) {
                    if (Math.abs(enemy.y - y + 30) < 10 && enemy.x < x + 50 && enemy.x + enemy.size > x && y + 80 > enemy.y) {
                        vy = -vy + 2;
                        y += 5;
                    }
                    if (Math.abs(enemy.y - y + 30) < 10 && enemy.x < x + 50 && enemy.x + enemy.size > x && y + 20 < enemy.y) {
                        vy = -vy - 2;
                        y -= 5;
                    }
                }
            }
            for (; i < meteors.size(); i++) {
                for (int j = 0; j < bullets.size(); j++) {
                    if (i < meteors.size() && Math.abs(bullets.get(j).x - meteors.get(i).x) < 30 && Math.abs(bullets.get(j).y - meteors.get(i).y) < 30 && !meteors.get(i).getClass().toString().equals(EnemyBullet.class.toString())) {
                        bullets.remove(j);
                        meteors.remove(i);
                        Random r = new Random();
                        if (r.nextInt(50) == 30) {
                            dd = true;
                        }
                        if (r.nextInt(50) == 30) {
                            health++;
                        }
                        if (r.nextInt(50) == 30) {
                            lasercount++;
                        }
                        scr += 5;
                        j = 0;
                        i = 0;
                    }
                }
            }

            g2d.drawImage(img.getImage(), (int) x - 30, (int) y - 40, 60, 80, (img1, infoflags, x, y, width, height) -> false);
        }
    }

    public static void main(String[] args) {
        Random r = new Random();
        JFrame window = new JFrame("window");
        MyPanel Panel = new MyPanel();
        JLabel dd = new JLabel();

        Panel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mouseMoved(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Panel.mx = e.getX();
                Panel.my = e.getY();
            }
        });

        JLabel heal = new JLabel();
        Panel.add(heal);
        window.add(Panel);
        Panel.add(dd);
        JLabel score = new JLabel();
        JLabel lasers = new JLabel();
        JLabel tutorial = new JLabel("<html><p>Press space to activate laser</p><p>current level of difficulty: 6</p></html>");
        Panel.add(tutorial);
        tutorial.setFont(new Font("", 0, 20));
        timer = new Timer(40, e -> {
            window.getKeyListeners();
            tutorial.setText("");
            if (Panel.dd) {
                dd.setText("Double damage activated");

            } else {
                dd.setText("");
            }
            heal.setText("Health: " + Panel.health);
            double xlen = (int) (Panel.mx - Panel.x);
            int ylen = (int) (Panel.my - Panel.y);
            int time = (int) (Math.max(Math.abs(xlen), Math.abs(ylen)) / 10 + 1);
            if (Panel.vx < xlen / time) {
                Panel.vx += 0.3;
            } else {
                Panel.vx -= 0.3;
            }
            if (Panel.vy < ylen / time) {
                Panel.vy += 0.3;
            } else {
                Panel.vy -= 0.3;
            }

            Panel.x += Panel.vx;
            Panel.y += Panel.vy;
            if (Math.abs(Panel.x - Panel.mx) < 5 && Math.abs(Panel.y - Panel.my) < 5) {
                Panel.x = Panel.mx;
                Panel.vx = 0;
                Panel.y = Panel.my;
                Panel.vy = 0;
            }
            score.setText("Score: " + scr);
            int k = r.nextInt(31);
            k = r.nextInt(60 * (9 - hard.getValue()));
            if (k == 1) {
                Panel.enemies.add(new Wall(r.nextInt(Panel.getWidth() - 100), 100 + r.nextInt(200)));
            }
            if (!Main.boss) {
                k = r.nextInt(8 * (9 - hard.getValue()));
                if (k == 8) {
                    Panel.meteors.add(new Meteor(r.nextInt(8) - 4, r.nextInt(5) + 1, 50, r.nextInt(700), 0));
                }
                k = r.nextInt(40 * (9 - hard.getValue()));
                if (k == 1) {
                    Panel.enemies.add(new EnemyShip(100, r.nextInt(window.getWidth() - 30), 20));
                }

                k = r.nextInt(150 * (9 - hard.getValue()));
                if (k == 1) {
                    Main.boss = true;
                }

            } else {
                boolean flag = true;
                for (int i = 0; i < Panel.enemies.size(); i++) {
                    if (Panel.enemies.get(i).getClass() != Wall.class) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    Panel.enemies.add(new Boss());
                }
            }
            if (Panel.health <= 0) {
                Main.timer.stop();

            }
            lasers.setText("You have: " + Panel.lasercount + " lasers. ");
            //Panel.x += Panel.vx;
            //Panel.y += Panel.vy;
            //double r = Math.sqrt(Math.pow((Panel.mx - Panel.x), 2) +Math.pow((Panel.my - Panel.y), 2));
            //Panel.vy += -5*(Panel.y - Panel.my) /1.0/ r/ r;
            //Panel.vx += -5*(Panel.x - Panel.mx) / 1.0/ r/ r;
            //System.out.println(Panel.vx);
            Panel.repaint();
        });
        Panel.x = 300;
        Panel.y = 500;
        hard = new JScrollBar(JScrollBar.HORIZONTAL, 5, 1, 0, 6);
        hard.setPreferredSize(new Dimension(200, 30));
        hard.addAdjustmentListener(e -> tutorial.setText("<html><p>Press space to activate laser</p><p>current level of difficulty: " + (hard.getValue() + 1) + "</p></html>"));
        JPanel Panel1 = new JPanel();
        final JButton[] strtgame = {new JButton("Start game")};
        strtgame[0].setFocusable(false);
        strtgame[0].addActionListener(e -> {

            for (Component comp : Panel1.getComponents()) {
                comp.setFocusable(false);
            }
            for (Component comp : Panel.getComponents()) {
                comp.setFocusable(false);
            }

            window.remove(Panel1);

            strtgame[0].setVisible(false);
            hard.setVisible(false);
            Panel1.setVisible(false);
            Panel.setVisible(true);
            strtgame[0].setFocusable(false);
            hard.setFocusable(false);
            Panel.setFocusable(true);
            Panel.setVisible(true);
            window.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {

                    if (e.getKeyChar() == 'q') {
                        Panel.dd = !Panel.dd;
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {

                    if (e.getKeyChar() == ' ' && Panel.lasercount > 0 && timer.isRunning()) {
                        Panel.laseralive = 5;
                        Panel.laserx = (int) Panel.x;
                        Panel.lasery = (int) Panel.y;
                        for (int i = 0; i < Panel.meteors.size(); ) {

                            if (Math.abs(Panel.meteors.get(i).x - Panel.laserx + 15) < 30) {
                                Panel.meteors.remove(i);
                                scr += 5;
                            } else {
                                i++;
                            }
                        }
                        for (int i = 0; i < Panel.enemies.size(); i++) {
                            if (Math.abs(Panel.enemies.get(i).x - Panel.laserx + Panel.enemies.get(i).size / 2) < 30) {
                                Panel.enemies.get(i).health -= 5;
                            }
                        }
                        Panel.laseralive = 5;
                        Panel.lasercount -= 1;
                        Panel.laserx = (int) Panel.x;
                        Panel.lasery = (int) Panel.y;

                    }
                }
            });
            window.add(Panel);

            timer.start();
        });
        Panel1.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 1;

        Panel1.add(tutorial);
        Panel.add(lasers);
        Panel1.add(hard, c);
        c.gridy = 2;
        Panel.setVisible(false);
        Panel1.add(strtgame[0], c);

        Panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (Panel.reload > 0) {

                    return;
                }
                if (!Panel.dd) {
                    Panel.bullets.add(new Bullet("bullet.png", 0, -8, 30, (int) Panel.x, (int) Panel.y));
                } else {
                    Panel.bullets.add(new Bullet("bullet.png", 0, -8, 30, (int) Panel.x - 25, (int) Panel.y));
                    Panel.bullets.add(new Bullet("bullet.png", 0, -8, 30, (int) Panel.x + 5, (int) Panel.y));

                }
                Panel.reload = 15;
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        window.add(Panel1);
        Panel.setBackground(new Color(255, 255, 255));
        Panel.add(score, BorderLayout.NORTH);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(500, 500);
        window.setVisible(true);
    }
}