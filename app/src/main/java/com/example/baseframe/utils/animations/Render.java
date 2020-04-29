/*
 * MIT License
 *
 * Copyright (c) 2019 Gayan Kuruppu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.example.baseframe.utils.animations;

import android.animation.AnimatorSet;
import android.content.Context;
import android.view.animation.AccelerateDecelerateInterpolator;

/*
Kotlin version of the Render class
https://github.com/gayankuruppu/android-view-animations-kotlin/blob/master/library/src/main/java/render/animations/Render.kt
*/

public class Render {
    private long duration = 1000;
    private Context cx;
    private AnimatorSet animatorSet;

    // Render render = new Render(this)
    public Render  (Context cx) {
        this.cx = cx;
    }

    // render.setAnimation(Bounce.In(textView))
    public void setAnimation (AnimatorSet animationSet) {
        this.animatorSet = animationSet;
    }

    // render.start()
    public void start() {
        animatorSet.setDuration(duration);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }

    // render.setDuration(2000)
    public void setDuration(long duration){
        this.duration = duration;
    }


}
