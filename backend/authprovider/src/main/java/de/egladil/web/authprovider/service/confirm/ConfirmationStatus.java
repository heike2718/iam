//=====================================================
// Projekt: de.egladil.mkv.service
// (c) Heike Winkelvoß
//=====================================================

package de.egladil.web.authprovider.service.confirm;

/**
 * ConfirmationStatus
 */
public enum ConfirmationStatus {

	deletedActivation,
	expiredActivation,
	normalActivation,
	repeatedActivation;
}
