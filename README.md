# bezier
A simple Java module that calculates quadratic or cubic Bezier curves
The constructor takes in argument a double that will specify the curve's "smoothness", the smaller this number is, the smoother the curve will look (more points).
Consideration is being given to make this backwards (the larger the number is, the smoother the curve is).
calculateCurve takes in argument a list of three or four control points. The first point is the beginning of the curve and the last one is the end of the curve. The point(s) in between is/are the control point(s) of the curve.
