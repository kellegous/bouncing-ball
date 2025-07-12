import { Vector2D } from "./Vector2D";
import { Constants } from "./constants";

export class Ball {
  private static readonly SHOW_EDGE_PATH = false;
  private static readonly SHOW_CENTER_PATH = true;

  private position: Vector2D;
  private velocity: Vector2D;
  private angularVel: number;
  private orientation: number;
  private r: number;

  private cxPath: Vector2D[] | null = null;
  private rxPath: Vector2D[] | null = null;

  constructor(
    x: number = 0,
    y: number = 0,
    r: number = 20,
    vx: number = 0,
    vy: number = 0
  ) {
    this.position = new Vector2D(x, y);
    this.velocity = new Vector2D(vx, vy);
    this.angularVel = 0;
    this.orientation = 0;
    this.r = r;
  }

  draw(ctx: CanvasRenderingContext2D): void {
    ctx.save();

    // Draw paths if enabled
    if (Ball.SHOW_EDGE_PATH || Ball.SHOW_CENTER_PATH) {
      ctx.save();
      ctx.globalAlpha = 0.25;

      if (Ball.SHOW_CENTER_PATH && this.cxPath && this.cxPath.length > 1) {
        ctx.strokeStyle = "red";
        ctx.beginPath();
        ctx.moveTo(this.cxPath[0].i, this.cxPath[0].j);
        for (let i = 1; i < this.cxPath.length; i++) {
          ctx.lineTo(this.cxPath[i].i, this.cxPath[i].j);
        }
        ctx.stroke();
      }

      if (Ball.SHOW_EDGE_PATH && this.rxPath && this.rxPath.length > 1) {
        ctx.strokeStyle = "green";
        ctx.beginPath();
        ctx.moveTo(this.rxPath[0].i, this.rxPath[0].j);
        for (let i = 1; i < this.rxPath.length; i++) {
          ctx.lineTo(this.rxPath[i].i, this.rxPath[i].j);
        }
        ctx.stroke();
      }

      ctx.restore();
    }

    // Draw ball at position
    ctx.translate(this.position.i, this.position.j);

    // Draw rotating dynamic ball (with pattern to show rotation)
    ctx.save();
    ctx.rotate(this.orientation);

    // Draw dynamic ball background
    ctx.fillStyle = "#4ecdc4";
    ctx.beginPath();
    ctx.arc(0, 0, this.r, 0, Math.PI * 2);
    ctx.fill();

    // Draw rotation pattern
    ctx.strokeStyle = "rgba(0, 0, 0, 0.3)";
    ctx.lineWidth = 2;
    ctx.beginPath();
    ctx.moveTo(0, -this.r);
    ctx.lineTo(0, this.r);
    ctx.stroke();
    ctx.beginPath();
    ctx.moveTo(-this.r, 0);
    ctx.lineTo(this.r, 0);
    ctx.stroke();

    ctx.restore();

    // Draw static ball overlay
    ctx.fillStyle = "#ff6b6b";
    ctx.globalAlpha = 0.7;
    ctx.beginPath();
    ctx.arc(0, 0, this.r, 0, Math.PI * 2);
    ctx.fill();

    // Add ball outline
    ctx.globalAlpha = 1;
    ctx.strokeStyle = "#333";
    ctx.lineWidth = 1;
    ctx.beginPath();
    ctx.arc(0, 0, this.r, 0, Math.PI * 2);
    ctx.stroke();

    ctx.restore();
  }

  getX(): number {
    return this.position.i;
  }
  setX(x: number): void {
    this.position.i = x;
  }
  getY(): number {
    return this.position.j;
  }
  setY(y: number): void {
    this.position.j = y;
  }
  getVx(): number {
    return this.velocity.i;
  }
  setVx(vx: number): void {
    this.velocity.i = vx;
  }
  getVy(): number {
    return this.velocity.j;
  }
  setVy(vy: number): void {
    this.velocity.j = vy;
  }
  getRadius(): number {
    return this.r;
  }
  setRadius(r: number): void {
    this.r = r;
  }
  setAngularVelocity(av: number): void {
    this.angularVel = av;
  }

  computeFrictionlessCollisions(width: number, height: number): void {
    // Collisions
    if (this.position.j + this.r >= height) {
      // collide with floor
      this.velocity.j = -Constants.DAMPENING * this.velocity.j;
      this.position.j = height - this.r;
    } else if (this.position.j - this.r <= 0) {
      // collide with ceiling
      this.velocity.j = -Constants.DAMPENING * this.velocity.j;
      this.position.j = this.r;
    }
    if (this.position.i + this.r >= width) {
      // collide with right wall
      this.velocity.i = -Constants.DAMPENING * this.velocity.i;
      this.position.i = width - this.r;
    } else if (this.position.i - this.r <= 0) {
      // collide with left wall
      this.velocity.i = -Constants.DAMPENING * this.velocity.i;
      this.position.i = this.r;
    }
  }

  static computeVxGarwin(vx: number, av: number, r: number): number {
    return (
      ((1 - Constants.GARWIN_ALPHA * Constants.Ex) * vx +
        Constants.GARWIN_ALPHA * (1 + Constants.Ex) * r * av) /
      (1 + Constants.GARWIN_ALPHA)
    );
  }

  static computeVyGarwin(vy: number): number {
    return -Constants.Ey * vy;
  }

  static computeAVGarwin(vx: number, av: number, r: number): number {
    return (
      ((1 + Constants.Ex) * vx +
        (Constants.GARWIN_ALPHA - Constants.Ex) * r * av) /
      (r * (1 + Constants.GARWIN_ALPHA))
    );
  }

  computeGarwinCollisions(width: number, height: number): void {
    if (this.position.j + this.r >= height) {
      this.velocity.i = Ball.computeVxGarwin(
        this.velocity.i,
        this.angularVel,
        this.r
      );
      this.angularVel = Ball.computeAVGarwin(
        this.velocity.i,
        this.angularVel,
        this.r
      );
      this.velocity.j = Ball.computeVyGarwin(this.velocity.j);
      this.position.j = height - this.r;
    }

    if (this.position.i + this.r >= width) {
      this.velocity.i = Ball.computeVyGarwin(this.velocity.i);
      this.velocity.j = Ball.computeVxGarwin(
        this.velocity.j,
        this.angularVel,
        this.r
      );
      this.angularVel = -Ball.computeAVGarwin(
        this.velocity.j,
        this.angularVel,
        this.r
      );
      this.position.i = width - this.r;
    } else if (this.position.i - this.r <= 0) {
      this.velocity.i = Ball.computeVyGarwin(this.velocity.i);
      this.velocity.j = Ball.computeVxGarwin(
        this.velocity.j,
        this.angularVel,
        this.r
      );
      this.angularVel = Ball.computeAVGarwin(
        this.velocity.j,
        this.angularVel,
        this.r
      );
      this.position.i = this.r;
    }
  }

  reset(): void {
    this.cxPath = null;
    this.rxPath = null;
    this.orientation = 0;
    this.angularVel = 0;
    this.velocity.set(0, 0);
  }

  update(dt: number, width: number, height: number): void {
    this.velocity.j = this.velocity.j + Constants.G * dt;
    this.position.i += this.velocity.i * dt;
    this.position.j += this.velocity.j * dt;
    this.orientation += this.angularVel * dt;
    this.computeGarwinCollisions(width, height);

    if (this.cxPath === null) {
      this.cxPath = [new Vector2D(this.position.i, this.position.j)];
    } else {
      this.cxPath.push(new Vector2D(this.position.i, this.position.j));
    }

    if (this.rxPath === null) {
      this.rxPath = [
        new Vector2D(
          this.position.i + this.r * Math.cos(this.orientation),
          this.position.j + this.r * Math.sin(this.orientation)
        ),
      ];
    } else {
      this.rxPath.push(
        new Vector2D(
          this.position.i + this.r * Math.cos(this.orientation),
          this.position.j + this.r * Math.sin(this.orientation)
        )
      );
    }
  }
}
