/// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain;

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
	CANCELLED {
		@Override
		public boolean isCompleted() {

			return true;
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
