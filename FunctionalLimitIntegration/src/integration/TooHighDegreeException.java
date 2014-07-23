package integration;

import org.apache.commons.math3.exception.TooManyEvaluationsException;

/**
 * Created by Xiaoxi Wang on 7/23/14.
 */
public class TooHighDegreeException extends TooManyEvaluationsException {

    /**
     * Construct the exception.
     *
     * @param max Maximum number of evaluations.
     */
    public TooHighDegreeException(Number max) {
        super(max);
    }
}
