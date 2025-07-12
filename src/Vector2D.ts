export class Vector2D {
  public i: number;
  public j: number;

  constructor(i: number = 0, j: number = 0) {
    this.i = i;
    this.j = j;
  }

  getLength(): number {
    return Math.sqrt(this.i * this.i + this.j * this.j);
  }

  normalize(): void {
    const l = this.getLength();
    this.i /= l;
    this.j /= l;
  }

  scale(s: number): void;
  scale(si: number, sj: number): void;
  scale(si: number, sj?: number): void {
    if (sj === undefined) {
      this.i *= si;
      this.j *= si;
    } else {
      this.i *= si;
      this.j *= sj;
    }
  }

  translate(di: number, dj: number): void {
    this.i += di;
    this.j += dj;
  }

  add(v: Vector2D): void {
    this.i += v.i;
    this.j += v.j;
  }

  set(i: number, j: number): void {
    this.i = i;
    this.j = j;
  }

  static dotProduct(v1: Vector2D, v2: Vector2D): number {
    return v1.i * v2.i + v1.j * v2.j;
  }

  static crossProduct(v1: Vector2D, v2: Vector2D): Vector2D {
    return new Vector2D(v1.i * v2.i, v2.j * v2.j);
  }

  toString(): string {
    return `(${this.i}, ${this.j})`;
  }
}
