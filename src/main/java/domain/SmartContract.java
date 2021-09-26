package domain;

import exceptions.InvalidCodeException;
import exceptions.InvalidPublicKeyException;
import org.python.util.PythonInterpreter;

import java.io.ByteArrayOutputStream;

public class SmartContract extends Contract {

    private String code;
    private String compiledCode;

    public SmartContract(String sender, String code) throws InvalidPublicKeyException, InvalidCodeException {
        super(sender);
        setCode(code);
        setCompiledCode();
    }

    public void setCode(String code) throws InvalidCodeException {
        if (code.equals("")) {
            throw new InvalidCodeException();
        } else {
            this.code = code;
        }
    }

    public void setCompiledCode() {
        this.compiledCode = runCode();
    }

    public String getCompiledCode() throws InvalidCodeException {
        return compiledCode;
    }


    public String runCode() {
        PythonInterpreter pythonInterpreter = new PythonInterpreter();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        pythonInterpreter.setOut(bout);
        pythonInterpreter.exec(code);
        return bout.toString().trim();
    }

    @Override
    public String generateStringToHash() {
        return super.generateStringToHash() + code;
    }

    @Override
    public String toString() {
        try {
            return "\nSender: " + getSender() +
                    "\nCode-Output: " + getCompiledCode() +
                    "\nSignature: " + getSignature();
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
