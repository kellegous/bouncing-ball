import { Ball } from "./Ball";
import { Vector2D } from "./Vector2D";
import { Constants } from "./constants";
import "./main.scss";

interface BallPanel {
  canvas: HTMLCanvasElement;
  ctx: CanvasRenderingContext2D;
  ball: Ball;
  label: string;
  initVelocity: Vector2D;
  initAngVelocity: number;
}

class BouncingBallSimulation {
  private time: number = 0;
  private readonly RUN_UNTIL = 40;
  private timer: number | null = null;
  private timeLabel!: HTMLElement;
  private panels: BallPanel[] = [];

  constructor() {
    this.setupUI();
    this.setupPanels();
    this.start();
  }

  private setupUI(): void {
    const app = document.getElementById("app");
    if (!app) return;

    app.innerHTML = `
            <div class="simulation-container">
                <div class="control-panel">
                    <span class="time-label">Time: 0.00</span>
                    <button class="reset-btn">Reset</button>
                </div>
                <div class="panels-container">
                    <div class="panel-wrapper">
                        <canvas id="canvas-no-spin" width="300" height="400"></canvas>
                        <div class="panel-label">No Spin</div>
                    </div>
                    <div class="panel-wrapper">
                        <canvas id="canvas-top-spin" width="300" height="400"></canvas>
                        <div class="panel-label">Top Spin</div>
                    </div>
                    <div class="panel-wrapper">
                        <canvas id="canvas-back-spin" width="300" height="400"></canvas>
                        <div class="panel-label">Back Spin</div>
                    </div>
                </div>
            </div>
        `;

    this.timeLabel = document.querySelector(".time-label")!;
    const resetBtn = document.querySelector(".reset-btn")!;
    resetBtn.addEventListener("click", () => this.reset());
  }

  private setupPanels(): void {
    const panelConfigs = [
      {
        id: "canvas-no-spin",
        label: "No Spin",
        velocity: new Vector2D(9, 5),
        angVel: 0,
      },
      {
        id: "canvas-top-spin",
        label: "Top Spin",
        velocity: new Vector2D(9, 5),
        angVel: 1.0,
      },
      {
        id: "canvas-back-spin",
        label: "Back Spin",
        velocity: new Vector2D(9, 5),
        angVel: -1.0,
      },
    ];

    panelConfigs.forEach((config) => {
      const canvas = document.getElementById(config.id) as HTMLCanvasElement;
      const ctx = canvas.getContext("2d")!;

      const ball = new Ball();

      const panel: BallPanel = {
        canvas,
        ctx,
        ball,
        label: config.label,
        initVelocity: config.velocity,
        initAngVelocity: config.angVel,
      };

      this.panels.push(panel);
      this.resetPanel(panel);
    });
  }

  private resetPanel(panel: BallPanel): void {
    panel.ball.reset();
    const r = panel.ball.getRadius();
    panel.ball.setX(r + 10);
    panel.ball.setY(r + 10);
    panel.ball.setAngularVelocity(panel.initAngVelocity);
    panel.ball.setVx(panel.initVelocity.i);
    panel.ball.setVy(panel.initVelocity.j);
  }

  private updateSimulation(): void {
    this.time += Constants.DELTA_T;
    this.timeLabel.textContent = `Time: ${this.time.toFixed(2)}`;

    this.panels.forEach((panel) => {
      panel.ball.update(
        Constants.DELTA_T,
        panel.canvas.width,
        panel.canvas.height
      );
      this.drawPanel(panel);
    });

    if (this.time > this.RUN_UNTIL && this.timer) {
      clearInterval(this.timer);
      this.timer = null;
    }
  }

  private drawPanel(panel: BallPanel): void {
    const { ctx, canvas, ball } = panel;

    // Clear canvas
    ctx.fillStyle = "white";
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    // Draw ball
    ball.draw(ctx);
  }

  private start(): void {
    if (this.timer) return;

    this.timer = setInterval(() => {
      this.updateSimulation();
    }, 20); // 20ms interval like the original Java applet
  }

  private reset(): void {
    this.time = 0;
    this.panels.forEach((panel) => this.resetPanel(panel));

    if (!this.timer) {
      this.start();
    }
  }
}

// Initialize the simulation when DOM is loaded
document.addEventListener("DOMContentLoaded", () => {
  new BouncingBallSimulation();
});
