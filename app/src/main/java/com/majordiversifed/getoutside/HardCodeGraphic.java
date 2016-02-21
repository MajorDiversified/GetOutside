package com.majordiversifed.getoutside;

import android.graphics.Color;
import android.location.Location;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;

/**
 * Created by Victor on 2/21/2016.
 *
 * @author Victor
 */
public class HardCodeGraphic {
    private Graphic[] graphics;

    public HardCodeGraphic() {
        this.graphics = this.makeGraphics();
    }

    public Graphic[] makeGraphics() {

        double[] lat =
                {
                        5205146.8,
                        5205197.4,
                        5205424.0,
                        5205146.7,
                        5204814.7,
                        5204602.9,
                        5207278.2,
                        5203039.7,
                        5202552.7,
                        5202419.5,
                        5202394.9,
                        5203119.3,
                        5202997.1,
                        5203295.9,
                        5202778.1,
                        5202712.7
                };


        double[] longitude =
                {
                    -9321311.9,
                    -9321610.6,
                    -9321614.8,
                    -9321642.6,
                    -9321086.2,
                    -9318995.9,
                    -9319888.6,
                    -9319843.6,
                    -9319902.3,
                    -9319809.7,
                    -9319878.2,
                    -9319557.8,
                    -9318952.0,
                    -9322515.9,
                    -9320875.5,
                    -9318147.4
                };


        this.graphics = new Graphic[16];
        for (int i = 0; i < lat.length; i++) {
            graphics[i] = new Graphic(new Point(longitude[i],lat[i])
                    , new SimpleMarkerSymbol(R.color.sms, 10, SimpleMarkerSymbol.STYLE.CIRCLE));
        }
        return graphics;
    }

    public void addGraphic (Point p) {
        SimpleMarkerSymbol simpleMarker = new SimpleMarkerSymbol(R.color.sms, 10, SimpleMarkerSymbol.STYLE.CIRCLE);
        Graphic[] replace = new Graphic[this.graphics.length + 1];
        for(int i = 0; i< this.graphics.length; ) {
            replace[i] = graphics[i];
        }
        this.graphics = replace;
        this.graphics[this.graphics.length - 1] = new Graphic(p, simpleMarker);
    }




    public void removeGraphic (int i) {

    }

    public Graphic[] getGraphics() {
        return graphics;
    }


}
