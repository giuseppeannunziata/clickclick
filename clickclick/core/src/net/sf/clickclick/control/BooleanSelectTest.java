package net.sf.clickclick.control;

import junit.framework.TestCase;
import net.sf.click.MockContext;
import net.sf.click.servlet.MockRequest;

public class BooleanSelectTest extends TestCase {

    public void testSetGetValue() {
        BooleanSelect bsField = new BooleanSelect("bs");

        bsField.setValue("true");
        assertEquals(Boolean.TRUE.toString(), bsField.getValue());
        bsField.setValue("false");
        assertEquals(Boolean.FALSE.toString(), bsField.getValue());
        bsField.setValue("trUe");
        assertEquals(Boolean.TRUE.toString(), bsField.getValue());
        bsField.setValue("fAlsE");
        assertEquals(Boolean.FALSE.toString(), bsField.getValue());
        bsField.setValue("");
        assertTrue(bsField.getValue().isEmpty());
        bsField.setValue(null);
        assertTrue(bsField.getValue().isEmpty());
        try {
            bsField.setValue("Something else");
            fail("should not be allowed");
        } catch (Exception e) {
        }
        try {
            bsField.setValue("1");
            fail("should not be allowed");
        } catch (Exception e) {
        }
    }

    public void testSetValueObject() {
        BooleanSelect bsField = new BooleanSelect("bs");

        bsField.setValueObject(Boolean.TRUE);
        assertEquals(Boolean.TRUE.toString(), bsField.getValue());
        bsField.setValueObject(Boolean.FALSE);
        assertEquals(Boolean.FALSE.toString(), bsField.getValue());
        bsField.setValueObject("true");
        assertEquals(Boolean.TRUE.toString(), bsField.getValue());
        bsField.setValueObject("false");
        assertEquals(Boolean.FALSE.toString(), bsField.getValue());
        bsField.setValueObject("");
        assertTrue(bsField.getValue().isEmpty());
        bsField.setValueObject(null);
        assertTrue(bsField.getValue().isEmpty());

        try {
            bsField.setValueObject("Something else");
            fail("should not be allowed");
        } catch (Exception e) {
        }
        try {
            bsField.setValueObject(this);
            fail("should not be allowed");
        } catch (Exception e) {
        }
    }

    public void testOnProcess() {
        MockContext mockContext = MockContext.initContext();
        MockRequest request = mockContext.getMockRequest();

        BooleanSelect bsField = new BooleanSelect("bs");
        assertEquals("bs", bsField.getName());

        // -- bad value - somebody tampered with the formdata
        request.getParameterMap().put("bs", "bad bad");
        assertTrue(bsField.onProcess());
        assertTrue(bsField.isValid());
        assertTrue(bsField.getValue().isEmpty());

        // -- true
        request.getParameterMap().put("bs", "true");
        assertTrue(bsField.onProcess());
        assertTrue(bsField.isValid());
        assertEquals(Boolean.TRUE, bsField.getBoolean());

        // -- false
        request.getParameterMap().put("bs", "false");
        assertTrue(bsField.onProcess());
        assertTrue(bsField.isValid());
        assertEquals(Boolean.FALSE, bsField.getBoolean());

        // -- no option selected
        bsField.setRequired(true);
        request.getParameterMap().remove("bs");
        assertTrue(bsField.onProcess());
        assertFalse(bsField.isValid());
    }
}
