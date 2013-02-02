/*
 * Copyright by Apaq 2011-2013
 */
package dk.apaq.simplepay.controllers;

import dk.apaq.simplepay.controllers.exceptions.ForbiddenException;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.security.SecurityHelper;

/**
 * Javadoc
 */
public class ControllerUtil {

    public static Merchant getMerchant(IPayService service) {
        Merchant merchant = SecurityHelper.getMerchant(service);
        if (merchant == null) {
            throw new ForbiddenException("No merchant found for user.");
        }
        return merchant;
    }
}
