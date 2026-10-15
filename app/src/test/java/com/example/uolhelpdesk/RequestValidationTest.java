package com.example.uolhelpdesk;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Local unit tests for the input checks used on the request screen.
 */
public class RequestValidationTest {

    @Test
    public void acceptsElevenDigitNumberStartingWith03() {
        assertTrue(RequestActivity.isValidMobileNumber("03001234567"));
        assertTrue(RequestActivity.isValidMobileNumber("03219876543"));
    }

    @Test
    public void rejectsNumbersWithWrongLengthOrPrefix() {
        assertFalse(RequestActivity.isValidMobileNumber("0300123456"));    // 10 digits
        assertFalse(RequestActivity.isValidMobileNumber("030012345678"));  // 12 digits
        assertFalse(RequestActivity.isValidMobileNumber("04212345678"));   // landline prefix
        assertFalse(RequestActivity.isValidMobileNumber("+923001234567")); // country code
        assertFalse(RequestActivity.isValidMobileNumber("0300-123456"));   // dash
    }

    @Test
    public void addsHttpsWhenSchemeIsMissing() {
        assertEquals("https://uol.edu.pk", RequestActivity.toWebLink("uol.edu.pk"));
        assertEquals("https://www.uol.edu.pk", RequestActivity.toWebLink("www.uol.edu.pk"));
    }

    @Test
    public void keepsExistingHttpOrHttpsScheme() {
        assertEquals("https://uol.edu.pk", RequestActivity.toWebLink("https://uol.edu.pk"));
        assertEquals("http://example.com", RequestActivity.toWebLink("http://example.com"));
        assertEquals("HTTPS://UOL.EDU.PK", RequestActivity.toWebLink("HTTPS://UOL.EDU.PK"));
    }
}
