/*
 * Decompiled with CFR 0.152.
 */
package CLib;

import model.CRes;

public class EasingFunction {
    private static float NATURAL_LOG_OF_2 = 0.6931472f;

    private static double ln(double d) {
        double d2 = (d - 1.0) / (d + 1.0);
        double d3 = d2 * d2;
        double d4 = d2;
        double d5 = d2;
        for (int i = 1; i < 24; ++i) {
            d4 += (d5 *= d3) / (double)(2 * i + 1);
        }
        return 2.0 * d4;
    }

    private static double exp(double d) {
        double d2 = 1.0;
        double d3 = 1.0;
        for (int i = 1; i < 30; ++i) {
            d2 += (d3 *= d / (double)i);
        }
        return d2;
    }

    private static double pow(double d, double d2) {
        if (d == 0.0) {
            return d2 == 0.0 ? 1.0 : 0.0;
        }
        return EasingFunction.exp(d2 * EasingFunction.ln(Math.abs(d)));
    }

    private static double asin(double d) {
        double d2 = d;
        double d3 = d;
        double d4 = d * d;
        for (int i = 1; i < 24; ++i) {
            d2 += (d3 *= d4 * (double)(2 * i - 1) * (double)(2 * i - 1) / ((double)(2 * i) * (double)(2 * i + 1)));
        }
        return d2;
    }

    public static float Linear(float f, float f2, float f3) {
        return CRes.Lerp(f, f2, f3);
    }

    public static float Spring(float f, float f2, float f3) {
        f3 = CRes.Clamp01(f3);
        f3 = (float)((Math.sin(f3 * 3.14f * (0.2f + 2.5f * f3 * f3 * f3)) * EasingFunction.pow(1.0f - f3, 2.2f) + (double)f3) * (double)(1.0f + 1.2f * (1.0f - f3)));
        return f + (f2 - f) * f3;
    }

    public static float EaseInQuad(float f, float f2, float f3) {
        return (f2 -= f) * f3 * f3 + f;
    }

    public static float EaseOutQuad(float f, float f2, float f3) {
        return -(f2 -= f) * f3 * (f3 - 2.0f) + f;
    }

    public static float EaseInOutQuad(float f, float f2, float f3) {
        f3 /= 0.5f;
        f2 -= f;
        if (f3 < 1.0f) {
            return f2 * 0.5f * f3 * f3 + f;
        }
        return -f2 * 0.5f * ((f3 -= 1.0f) * (f3 - 2.0f) - 1.0f) + f;
    }

    public static float EaseInCubic(float f, float f2, float f3) {
        return (f2 -= f) * f3 * f3 * f3 + f;
    }

    public static float EaseOutCubic(float f, float f2, float f3) {
        return (f2 -= f) * ((f3 -= 1.0f) * f3 * f3 + 1.0f) + f;
    }

    public static float EaseInOutCubic(float f, float f2, float f3) {
        f3 /= 0.5f;
        f2 -= f;
        if (f3 < 1.0f) {
            return f2 * 0.5f * f3 * f3 * f3 + f;
        }
        return f2 * 0.5f * ((f3 -= 2.0f) * f3 * f3 + 2.0f) + f;
    }

    public static float EaseInQuart(float f, float f2, float f3) {
        return (f2 -= f) * f3 * f3 * f3 * f3 + f;
    }

    public static float EaseOutQuart(float f, float f2, float f3) {
        return -(f2 -= f) * ((f3 -= 1.0f) * f3 * f3 * f3 - 1.0f) + f;
    }

    public static float EaseInOutQuart(float f, float f2, float f3) {
        f3 /= 0.5f;
        f2 -= f;
        if (f3 < 1.0f) {
            return f2 * 0.5f * f3 * f3 * f3 * f3 + f;
        }
        return -f2 * 0.5f * ((f3 -= 2.0f) * f3 * f3 * f3 - 2.0f) + f;
    }

    public static float EaseInQuint(float f, float f2, float f3) {
        return (f2 -= f) * f3 * f3 * f3 * f3 * f3 + f;
    }

    public static float EaseOutQuint(float f, float f2, float f3) {
        return (f2 -= f) * ((f3 -= 1.0f) * f3 * f3 * f3 * f3 + 1.0f) + f;
    }

    public static float EaseInOutQuint(float f, float f2, float f3) {
        f3 /= 0.5f;
        f2 -= f;
        if (f3 < 1.0f) {
            return f2 * 0.5f * f3 * f3 * f3 * f3 * f3 + f;
        }
        return f2 * 0.5f * ((f3 -= 2.0f) * f3 * f3 * f3 * f3 + 2.0f) + f;
    }

    public static float EaseInSine(float f, float f2, float f3) {
        return (float)((double)(-(f2 -= f)) * Math.cos((double)f3 * 1.5707963267948966) + (double)f2 + (double)f);
    }

    public static float EaseOutSine(float f, float f2, float f3) {
        return (float)((double)(f2 -= f) * Math.sin((double)f3 * 1.5707963267948966) + (double)f);
    }

    public static float EaseInOutSine(float f, float f2, float f3) {
        return (float)((double)(-(f2 -= f) * 0.5f) * (Math.cos(Math.PI * (double)f3) - 1.0) + (double)f);
    }

    public static float EaseInExpo(float f, float f2, float f3) {
        return (float)((double)(f2 -= f) * EasingFunction.pow(2.0, 10.0f * (f3 - 1.0f)) + (double)f);
    }

    public static float EaseOutExpo(float f, float f2, float f3) {
        return (float)((double)(f2 -= f) * (-EasingFunction.pow(2.0, -10.0f * f3) + 1.0) + (double)f);
    }

    public static float EaseInOutExpo(float f, float f2, float f3) {
        f3 /= 0.5f;
        f2 -= f;
        if (f3 < 1.0f) {
            return (float)((double)(f2 * 0.5f) * EasingFunction.pow(2.0, 10.0f * (f3 - 1.0f)) + (double)f);
        }
        return (float)((double)(f2 * 0.5f) * (-EasingFunction.pow(2.0, -10.0f * (f3 -= 1.0f)) + 2.0) + (double)f);
    }

    public static float EaseInCirc(float f, float f2, float f3) {
        return (float)((double)(-(f2 -= f)) * (Math.sqrt(1.0f - f3 * f3) - 1.0) + (double)f);
    }

    public static float EaseOutCirc(float f, float f2, float f3) {
        return (float)((double)(f2 -= f) * Math.sqrt(1.0f - (f3 -= 1.0f) * f3) + (double)f);
    }

    public static float EaseInOutCirc(float f, float f2, float f3) {
        f3 /= 0.5f;
        f2 -= f;
        if (f3 < 1.0f) {
            return (float)((double)(-f2 * 0.5f) * (Math.sqrt(1.0f - f3 * f3) - 1.0) + (double)f);
        }
        return (float)((double)(f2 * 0.5f) * (Math.sqrt(1.0f - (f3 -= 2.0f) * f3) + 1.0) + (double)f);
    }

    public static float EaseInBounce(float f, float f2, float f3) {
        float f4 = 1.0f;
        return (f2 -= f) - EasingFunction.EaseOutBounce(0.0f, f2, f4 - f3) + f;
    }

    public static float EaseOutBounce(float f, float f2, float f3) {
        f3 /= 1.0f;
        f2 -= f;
        if (f3 < 0.36363637f) {
            return f2 * 7.5625f * f3 * f3 + f;
        }
        if (f3 < 0.72727275f) {
            return f2 * (7.5625f * (f3 -= 0.54545456f) * f3 + 0.75f) + f;
        }
        if ((double)f3 < 0.9090909090909091) {
            return f2 * (7.5625f * (f3 -= 0.8181818f) * f3 + 0.9375f) + f;
        }
        return f2 * (7.5625f * (f3 -= 0.95454544f) * f3 + 0.984375f) + f;
    }

    public static float EaseInOutBounce(float f, float f2, float f3) {
        float f4 = 1.0f;
        return f3 < f4 * 0.5f ? EasingFunction.EaseInBounce(0.0f, f2, f3 * 2.0f) * 0.5f + f : EasingFunction.EaseOutBounce(0.0f, f2 -= f, f3 * 2.0f - f4) * 0.5f + f2 * 0.5f + f;
    }

    public static float EaseInBack(float f, float f2, float f3) {
        float f4 = 1.70158f;
        return (f2 -= f) * (f3 /= 1.0f) * f3 * ((f4 + 1.0f) * f3 - f4) + f;
    }

    public static float EaseOutBack(float f, float f2, float f3) {
        float f4 = 1.70158f;
        return (f2 -= f) * ((f3 -= 1.0f) * f3 * ((f4 + 1.0f) * f3 + f4) + 1.0f) + f;
    }

    public static float EaseInOutBack(float f, float f2, float f3) {
        float f4 = 1.70158f;
        f2 -= f;
        if ((f3 /= 0.5f) < 1.0f) {
            return f2 * 0.5f * f3 * f3 * (((f4 *= 1.525f) + 1.0f) * f3 - f4) + f;
        }
        return f2 * 0.5f * ((f3 -= 2.0f) * f3 * (((f4 *= 1.525f) + 1.0f) * f3 + f4) + 2.0f) + f;
    }

    public static float EaseInElastic(float f, float f2, float f3) {
        float f4;
        f2 -= f;
        float f5 = 1.0f;
        float f6 = f5 * 0.3f;
        float f7 = 0.0f;
        if (f3 == 0.0f) {
            return f;
        }
        if ((f3 /= f5) == 1.0f) {
            return f + f2;
        }
        if (f7 != 0.0f && !(f7 < Math.abs(f2))) {
            f4 = (float)((double)f6 / (Math.PI * 2) * EasingFunction.asin(f2 / f7));
        } else {
            f7 = f2;
            f4 = f6 / 4.0f;
        }
        return (float)(-((double)f7 * EasingFunction.pow(2.0, 10.0f * (f3 -= 1.0f)) * Math.sin((double)(f3 * f5 - f4) * (Math.PI * 2) / (double)f6)) + (double)f);
    }

    public static float EaseOutElastic(float f, float f2, float f3) {
        float f4;
        f2 -= f;
        float f5 = 1.0f;
        float f6 = f5 * 0.3f;
        float f7 = 0.0f;
        if (f3 == 0.0f) {
            return f;
        }
        if ((f3 /= f5) == 1.0f) {
            return f + f2;
        }
        if (f7 != 0.0f && !(f7 < Math.abs(f2))) {
            f4 = (float)((double)f6 / (Math.PI * 2) * EasingFunction.asin(f2 / f7));
        } else {
            f7 = f2;
            f4 = f6 * 0.25f;
        }
        return (float)((double)f7 * EasingFunction.pow(2.0, -10.0f * f3) * Math.sin((double)(f3 * f5 - f4) * (Math.PI * 2) / (double)f6) + (double)f2 + (double)f);
    }

    public static float EaseInOutElastic(float f, float f2, float f3) {
        float f4;
        f2 -= f;
        float f5 = 1.0f;
        float f6 = f5 * 0.3f;
        float f7 = 0.0f;
        if (f3 == 0.0f) {
            return f;
        }
        if ((f3 /= f5 * 0.5f) == 2.0f) {
            return f + f2;
        }
        if (f7 != 0.0f && !(f7 < Math.abs(f2))) {
            f4 = (float)((double)f6 / (Math.PI * 2) * EasingFunction.asin(f2 / f7));
        } else {
            f7 = f2;
            f4 = f6 / 4.0f;
        }
        return f3 < 1.0f ? (float)(-0.5 * (double)f7 * EasingFunction.pow(2.0, 10.0f * (f3 -= 1.0f)) * Math.sin((double)(f3 * f5 - f4) * (Math.PI * 2) / (double)f6) + (double)f) : (float)((double)f7 * EasingFunction.pow(2.0, -10.0f * (f3 -= 1.0f)) * Math.sin((double)(f3 * f5 - f4) * (Math.PI * 2) / (double)f6) * 0.5 + (double)f2 + (double)f);
    }

    public static float LinearD(float f, float f2, float f3) {
        return f2 - f;
    }

    public static float EaseInQuadD(float f, float f2, float f3) {
        return 2.0f * (f2 - f) * f3;
    }

    public static float EaseOutQuadD(float f, float f2, float f3) {
        return -(f2 -= f) * f3 - f2 * (f3 - 2.0f);
    }

    public static float EaseInOutQuadD(float f, float f2, float f3) {
        f3 /= 0.5f;
        f2 -= f;
        if (f3 < 1.0f) {
            return f2 * f3;
        }
        return f2 * (1.0f - (f3 -= 1.0f));
    }

    public static float EaseInCubicD(float f, float f2, float f3) {
        return 3.0f * (f2 - f) * f3 * f3;
    }

    public static float EaseOutCubicD(float f, float f2, float f3) {
        return 3.0f * (f2 -= f) * (f3 -= 1.0f) * f3;
    }

    public static float EaseInOutCubicD(float f, float f2, float f3) {
        f3 /= 0.5f;
        f2 -= f;
        if (f3 < 1.0f) {
            return 1.5f * f2 * f3 * f3;
        }
        return 1.5f * f2 * (f3 -= 2.0f) * f3;
    }

    public static float EaseInQuartD(float f, float f2, float f3) {
        return 4.0f * (f2 - f) * f3 * f3 * f3;
    }

    public static float EaseOutQuartD(float f, float f2, float f3) {
        return -4.0f * (f2 -= f) * (f3 -= 1.0f) * f3 * f3;
    }

    public static float EaseInOutQuartD(float f, float f2, float f3) {
        f3 /= 0.5f;
        f2 -= f;
        if (f3 < 1.0f) {
            return 2.0f * f2 * f3 * f3 * f3;
        }
        return -2.0f * f2 * (f3 -= 2.0f) * f3 * f3;
    }

    public static float EaseInQuintD(float f, float f2, float f3) {
        return 5.0f * (f2 - f) * f3 * f3 * f3 * f3;
    }

    public static float EaseOutQuintD(float f, float f2, float f3) {
        return 5.0f * (f2 -= f) * (f3 -= 1.0f) * f3 * f3 * f3;
    }

    public static float EaseInOutQuintD(float f, float f2, float f3) {
        f3 /= 0.5f;
        f2 -= f;
        if (f3 < 1.0f) {
            return 2.5f * f2 * f3 * f3 * f3 * f3;
        }
        return 2.5f * f2 * (f3 -= 2.0f) * f3 * f3 * f3;
    }

    public static float EaseInSineD(float f, float f2, float f3) {
        return (float)((double)((f2 - f) * 0.5f) * Math.PI * Math.sin(1.5707963267948966 * (double)f3));
    }

    public static float EaseOutSineD(float f, float f2, float f3) {
        return (float)(1.5707963267948966 * (double)(f2 -= f) * Math.cos((double)f3 * 1.5707963267948966));
    }

    public static float EaseInOutSineD(float f, float f2, float f3) {
        return (float)((double)((f2 -= f) * 0.5f) * Math.PI * Math.sin(Math.PI * (double)f3));
    }

    public static float EaseInExpoD(float f, float f2, float f3) {
        return (float)((double)(10.0f * NATURAL_LOG_OF_2 * (f2 - f)) * EasingFunction.pow(2.0, 10.0f * (f3 - 1.0f)));
    }

    public static float EaseOutExpoD(float f, float f2, float f3) {
        return (float)((double)(5.0f * NATURAL_LOG_OF_2 * (f2 -= f)) * EasingFunction.pow(2.0, 1.0f - 10.0f * f3));
    }

    public static float EaseInOutExpoD(float f, float f2, float f3) {
        f3 /= 0.5f;
        f2 -= f;
        if (f3 < 1.0f) {
            return (float)((double)(5.0f * NATURAL_LOG_OF_2 * f2) * EasingFunction.pow(2.0, 10.0f * (f3 - 1.0f)));
        }
        return (float)((double)(5.0f * NATURAL_LOG_OF_2 * f2) / EasingFunction.pow(2.0, 10.0f * (f3 -= 1.0f)));
    }

    public static float EaseInCircD(float f, float f2, float f3) {
        return (float)((double)((f2 - f) * f3) / Math.sqrt(1.0f - f3 * f3));
    }

    public static float EaseOutCircD(float f, float f2, float f3) {
        return (float)((double)(-(f2 -= f) * (f3 -= 1.0f)) / Math.sqrt(1.0f - f3 * f3));
    }

    public static float EaseInOutCircD(float f, float f2, float f3) {
        f3 /= 0.5f;
        f2 -= f;
        if (f3 < 1.0f) {
            return (float)((double)(f2 * f3) / (2.0 * Math.sqrt(1.0f - f3 * f3)));
        }
        return (float)((double)(-f2 * (f3 -= 2.0f)) / (2.0 * Math.sqrt(1.0f - f3 * f3)));
    }

    public static float EaseInBounceD(float f, float f2, float f3) {
        float f4 = 1.0f;
        return EasingFunction.EaseOutBounceD(0.0f, f2 -= f, f4 - f3);
    }

    public static float EaseOutBounceD(float f, float f2, float f3) {
        f3 /= 1.0f;
        f2 -= f;
        if (f3 < 0.36363637f) {
            return 2.0f * f2 * 7.5625f * f3;
        }
        if (f3 < 0.72727275f) {
            return 2.0f * f2 * 7.5625f * (f3 -= 0.54545456f);
        }
        if ((double)f3 < 0.9090909090909091) {
            return 2.0f * f2 * 7.5625f * (f3 -= 0.8181818f);
        }
        return 2.0f * f2 * 7.5625f * (f3 -= 0.95454544f);
    }

    public static float EaseInOutBounceD(float f, float f2, float f3) {
        float f4 = 1.0f;
        return f3 < f4 * 0.5f ? EasingFunction.EaseInBounceD(0.0f, f2, f3 * 2.0f) * 0.5f : EasingFunction.EaseOutBounceD(0.0f, f2 -= f, f3 * 2.0f - f4) * 0.5f;
    }

    public static float EaseInBackD(float f, float f2, float f3) {
        float f4 = 1.70158f;
        return 3.0f * (f4 + 1.0f) * (f2 - f) * f3 * f3 - 2.0f * f4 * (f2 - f) * f3;
    }

    public static float EaseOutBackD(float f, float f2, float f3) {
        float f4 = 1.70158f;
        return (f2 -= f) * ((f4 + 1.0f) * (f3 -= 1.0f) * f3 + 2.0f * f3 * ((f4 + 1.0f) * f3 + f4));
    }

    public static float EaseInOutBackD(float f, float f2, float f3) {
        float f4 = 1.70158f;
        f2 -= f;
        if ((f3 /= 0.5f) < 1.0f) {
            return 0.5f * f2 * ((f4 *= 1.525f) + 1.0f) * f3 * f3 + f2 * f3 * ((f4 + 1.0f) * f3 - f4);
        }
        return 0.5f * f2 * (((f4 *= 1.525f) + 1.0f) * (f3 -= 2.0f) * f3 + 2.0f * f3 * ((f4 + 1.0f) * f3 + f4));
    }

    public static float EaseInElasticD(float f, float f2, float f3) {
        return EasingFunction.EaseOutElasticD(f, f2, 1.0f - f3);
    }

    public static float EaseOutElasticD(float f, float f2, float f3) {
        float f4;
        float f5 = 1.0f;
        float f6 = f5 * 0.3f;
        float f7 = 0.0f;
        if (f7 != 0.0f && !(f7 < Math.abs(f2 -= f))) {
            f4 = (float)((double)f6 / (Math.PI * 2) * EasingFunction.asin(f2 / f7));
        } else {
            f7 = f2;
            f4 = f6 * 0.25f;
        }
        return (float)((double)f7 * Math.PI * (double)f5 * EasingFunction.pow(2.0, 1.0f - 10.0f * f3) * Math.cos(Math.PI * 2 * (double)(f5 * f3 - f4) / (double)f6) / (double)f6 - (double)(5.0f * NATURAL_LOG_OF_2 * f7) * EasingFunction.pow(2.0, 1.0f - 10.0f * f3) * Math.sin(Math.PI * 2 * (double)(f5 * f3 - f4) / (double)f6));
    }

    public static float EaseInOutElasticD(float f, float f2, float f3) {
        float f4;
        float f5 = 1.0f;
        float f6 = f5 * 0.3f;
        float f7 = 0.0f;
        if (f7 != 0.0f && !(f7 < Math.abs(f2 -= f))) {
            f4 = (float)((double)f6 / (Math.PI * 2) * EasingFunction.asin(f2 / f7));
        } else {
            f7 = f2;
            f4 = f6 / 4.0f;
        }
        if (f3 < 1.0f) {
            return (float)((double)(-5.0f * NATURAL_LOG_OF_2 * f7) * EasingFunction.pow(2.0, 10.0f * (f3 -= 1.0f)) * Math.sin(Math.PI * 2 * (double)(f5 * f3 - 2.0f) / (double)f6) - (double)f7 * Math.PI * (double)f5 * EasingFunction.pow(2.0, 10.0f * f3) * Math.cos(Math.PI * 2 * (double)(f5 * f3 - f4) / (double)f6) / (double)f6);
        }
        return (float)((double)f7 * Math.PI * (double)f5 * Math.cos(Math.PI * 2 * (double)(f5 * (f3 -= 1.0f) - f4) / (double)f6) / ((double)f6 * EasingFunction.pow(2.0, 10.0f * f3)) - (double)(5.0f * NATURAL_LOG_OF_2 * f7) * Math.sin(Math.PI * 2 * (double)(f5 * f3 - f4) / (double)f6) / EasingFunction.pow(2.0, 10.0f * f3));
    }

    public static float SpringD(float f, float f2, float f3) {
        f3 = CRes.Clamp01(f3);
        return (float)((double)((f2 -= f) * (6.0f * (1.0f - f3) / 5.0f + 1.0f)) * ((double)-2.2f * EasingFunction.pow(1.0f - f3, 1.2f) * Math.sin(Math.PI * (double)f3 * (double)(2.5f * f3 * f3 * f3 + 0.2f)) + EasingFunction.pow(1.0f - f3, 2.2f) * (Math.PI * (double)(2.5f * f3 * f3 * f3 + 0.2f) + 23.561944901923447 * (double)f3 * (double)f3 * (double)f3) * Math.cos(Math.PI * (double)f3 * (double)(2.5f * f3 * f3 * f3 + 0.2f)) + 1.0) - (double)(6.0f * f2) * (EasingFunction.pow(1.0f - f3, 2.2f) * Math.sin(Math.PI * (double)f3 * (double)(2.5f * f3 * f3 * f3 + 0.2f)) + (double)(f3 / 5.0f)));
    }
}

