export const Constants = {
  // CoR from Garwin's Model
  Ex: 0.65,
  Ey: 0.65,

  // From Garwin's Model
  GARWIN_ALPHA: 2 / 5,

  Mball: 5, // kg
  G: 9.8, // m/sec^2
  DELTA_T: 0.04, // second
  DAMPENING: 0.8, // ratio (this goes away)

  // Images would be loaded differently in web context
  STATIC_IMAGE: "images/lotto-hi.png",
  DYNAMIC_IMAGE: "images/lotto-lo.png",
} as const;
