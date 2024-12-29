/*package com.codegen.exception;

public class CodeGenerationException extends RuntimeException {
    public CodeGenerationException(String message) {
        super(message);
    }
}
*/
package com.codegen.exception;

public class CodeGenerationException extends RuntimeException {
    public CodeGenerationException(String message) {
        super(message);
    }

    public CodeGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}