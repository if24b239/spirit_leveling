package com.rain.spiritleveling.util;

import com.rain.spiritleveling.api.Element;
import org.joml.Vector2d;

public class SpiritMath {

    /**
     * @param elementA use FlowPos of this element as the starting point
     * @param elementB use FlowPos of this element as the end point
     * @return a value between -10 and 10
     */
    public static double calculateElementFlow(Element elementA, Element elementB) {
        return calculateElementFlow(elementA.getFlowPos(), elementB.getFlowPos());
    }

    /**
     * use this function call to avoid the (-5,-5) to (5,5) FlowPosition limitation of Element (might return > 10 or < -10)
     * @param pointA the starting point of the 'curve'
     * @param pointB the end point of the 'curve'
     * @return the Flow of Vector field F(x,y) = yî - xĵ (a basic circulation field) over the line pointA + (pointB - pointA)t, t∈[0,1]
     * <a href="https://www.geogebra.org/calculator/dvxhhhsk">Geogebra calculation</a>
     */
    public static double calculateElementFlow(Vector2d pointA, Vector2d pointB) {
        double flowFormula = pointB.x * pointA.y - pointA.x * pointB.y;
        double divisor = Math.log(Math.abs(flowFormula) * 0.64) / Math.log(2); // factor 0.64 because it allows for exact max of 10 and min of -10
        return flowFormula / divisor;
    }
}
