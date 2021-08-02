/* !!! DOOST !!! */

/*
   A Java implementation of BLAKE2B cryptographic digest algorithm.

   Joubin Mohammad Houshyar <alphazero@sensesay.net>
   bushwick, nyc
   02-14-2014

   --

   To the extent possible under law, the author(s) have dedicated all copyright
   and related and neighboring rights to this software to the public domain
   worldwide. This software is distributed without any warranty.

   You should have received a copy of the CC0 Public Domain Dedication along with
   this software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
*/

package ove.crypto.digest;

import static ove.crypto.digest.Blake2BTestUtils.Reference;
import static ove.crypto.digest.Blake2BTestUtils.blake2b_kat;
import static ove.crypto.digest.Blake2BTestUtils.loadKATData;
import static ove.test.Utils.dumpBuffer;
import static ove.test.Utils.logit;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test suite of message digest functionality checked against reference KAT data.
 */
abstract public class TestDigestAbstractBase extends Blake2bTestsBase {


	// ------------------------------------------------------------------
	// tests
	// ------------------------------------------------------------------

	/**  */
	@Test(suiteName = "blake2b")
	public void testFullUpdate() {
		logit (this, "testFullUpdate");

		// load refrence KAT data
		final byte[] refbytes = loadKATData( blake2b_kat );

		// get reference KAT input data (no key)
		final byte[] data = Reference.getKATInput();

		// generate KAT with test subject
		final Blake2b digest = newMessageDigest ();

		// see blake2b/ref/blake2b-ref.c
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream out = new PrintStream(baos);
		for(int i = 0; i <= data.length; i++) {
			digest.update(data, 0, i);
			byte[] hash = digest.digest ();
			dumpBuffer(out, hash, 0, hash.length);
		}

		// check
		byte[] testBytes = baos.toByteArray();
		Assert.assertEquals ( testBytes, refbytes, "does not match reference KAT");
	}

	/**  */
	@Test(suiteName = "blake2b")
	public void testSingleByteUpdates()  {
		logit (this, "testSingleByteUpdates");
		// load refrence KAT data
		final byte[] refbytes = loadKATData( blake2b_kat );

		// get reference KAT input data (no key)
		final byte[] data = Reference.getKATInput();

		// generate KAT with test subject
		final Blake2b digest = newMessageDigest ();

		// see blake2b/ref/blake2b-ref.c
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream out = new PrintStream(baos);

		// note: empty/nil-input is added to conform with KAT data
		byte[] hash = digest.digest();
		dumpBuffer(out, hash, 0, hash.length);

		// now try single byte updates for data input lengths of 1 or more
		for(int i = 0; i < data.length; i++) {
			for(int j = 0; j <= i; j++) {
				digest.update(data[j]);
			}
			hash = digest.digest ();
			dumpBuffer(out, hash, 0, hash.length);
		}

		// check
		byte[] testBytes = baos.toByteArray();
		Assert.assertEquals ( testBytes, refbytes, "does not match reference KAT");
	}
}
