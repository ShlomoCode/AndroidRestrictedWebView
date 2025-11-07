# NetFree CA Certificates Directory

This directory contains the CA (Certificate Authority) certificates required for NetFree support.

## Overview

This app is configured to work **ONLY with NetFree** internet filtering. The app:
- **Blocks all HTTP traffic** - only HTTPS connections are allowed
- **Uses certificate pinning** - trusts ONLY the CA certificates placed in this directory
- **Does NOT trust system certificates** - built-in Android certificates are ignored

## How to Add NetFree CA Certificates

1. Obtain the NetFree CA certificate files in `.crt` format (PEM or DER encoded)
2. Place the `.crt` files directly in this directory:
   ```
   app/src/main/res/raw/
   ```

3. Update the network security configuration:
   - Open: `app/src/main/res/xml/network_security_config.xml`
   - Add certificate references (without the `.crt` extension):
   ```xml
   <trust-anchors>
       <certificates src="@raw/netfree_ca" />
       <!-- Add more certificates if needed -->
   </trust-anchors>
   ```

## Example

If you have a certificate file named `netfree_ca.crt`:
1. Place it in: `app/src/main/res/raw/netfree_ca.crt`
2. Reference it as: `<certificates src="@raw/netfree_ca" />`

## Important Notes

- **File naming**: Certificate files must be lowercase and use only letters, numbers, and underscores
  - ✅ Good: `netfree_ca.crt`, `netfree_root.crt`
  - ❌ Bad: `NetFree-CA.crt`, `netfree.ca.crt`

- **Format**: Certificates must be in CRT format (PEM or DER encoded X.509 certificates)

- **Multiple certificates**: You can add multiple certificate files if NetFree uses a certificate chain

## Certificate Format Conversion

If you have a certificate in a different format, you can convert it using OpenSSL:

```bash
# Convert PEM to CRT (if needed)
openssl x509 -in netfree.pem -out netfree_ca.crt

# Convert DER to CRT
openssl x509 -inform der -in netfree.cer -out netfree_ca.crt
```

## Testing

After adding the certificates:
1. Build the app: `./gradlew assembleDebug`
2. Install on a device with NetFree active
3. The app should connect without SSL errors

If you see SSL errors, check:
- Certificate files are in the correct format
- Certificate files are properly referenced in `network_security_config.xml`
- The certificates match NetFree's current CA
