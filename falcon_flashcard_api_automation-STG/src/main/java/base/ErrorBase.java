package base;

import pojos.ErrorResponse;

/**
 * Base class for ErrorResponse.
 */
public class ErrorBase extends BaseClass {

    /**
     * Builder for ErrorResponse.
     *
     * @param status      String status
     * @param message     String message
     * @param code        String code
     * @param faultstring String Fault -> faultstring
     * @param errorcode   String Detail -> errorcode
     * @return ErrorResponse
     */
    protected ErrorResponse buildErrorResponse(final String status,
                                               final String message,
                                               final String code,
                                               final String faultstring,
                                               final String errorcode) {


//        final Detail detail = Detail.builder().errorcode(errorcode).build();
//        final Fault fault = Fault.builder().faultstring(faultstring).detail(detail).build();
//        return ErrorResponse.builder().status(status)
//                .message(message)
//                .code(code)
//                .fault(fault).build();
        return null;
    }

}
