/// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain;

/**
 * Jobstatus Status eines bestimmten Jobs, der asynchron ausgeführt wird.
 */
public enum Jobstatus {

	WAITING {

		@Override
		public boolean isCompleted() {

			return false;
		}

	},
	IN_PROGRESS {

		@Override
		public boolean isCompleted() {

			return false;
		}

	},
	COMPLETED {

		@Override
		public boolean isCompleted() {

			return true;
		}

	},
	ERRORS {

		@Override
		public boolean isCompleted() {

			return true;
		}

	};

	public abstract boolean isCompleted();

}
