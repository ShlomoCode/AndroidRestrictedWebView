# NetFree CA Certificates Directory

This directory contains the CA (Certificate Authority) certificates required for NetFree support.

## Overview

This app is configured to work **ONLY with NetFree** internet filtering. The app:
- **Blocks all HTTP traffic** - only HTTPS connections are allowed
- **Uses certificate pinning** - trusts ONLY the CA certificates placed in this directory
- **Does NOT trust system certificates** - built-in Android certificates are ignored

## How to Add NetFree CA Certificates

1. Obtain the NetFree CA certificate files in `.crt` format (PEM or DER encoded)

2. **IMPORTANT - File Naming**: Android resource files cannot start with numbers or contain dots (except the extension).

   **Rename these files BEFORE uploading**:
   - `018.crt` → `netfree_018.crt`
   - `019.crt` → `netfree_019.crt`
   - `099.crt` → `netfree_099.crt`

   **Also replace hyphens with underscores**:
   - `ib-itc.crt` → `ib_itc.crt`
   - `ib-partner.crt` → `ib_partner.crt`
   - `ib-spotnet.crt` → `ib_spotnet.crt`
   - `hadran-vpn.crt` → `hadran_vpn.crt`
   - `kosher-sim-cellcom.crt` → `kosher_sim_cellcom.crt`
   - `kosher-sim.crt` → `kosher_sim.crt`
   - `ksim-itc.crt` → `ksim_itc.crt`
   - `ksim-partner.crt` → `ksim_partner.crt`
   - `ksim-pele.crt` → `ksim_pele.crt`
   - `netfree-anywhere.crt` → `netfree_anywhere.crt`
   - `sim-kasher-triple-c.crt` → `sim_kasher_triple_c.crt`

3. Place the **renamed** `.crt` files directly in this directory:
   ```
   app/src/main/res/raw/
   ```

4. The network security configuration has already been updated in:
   `app/src/main/res/xml/network_security_config.xml`

   All 20 NetFree certificates are pre-configured!

## Complete File List (After Renaming)

Place these files in `app/src/main/res/raw/`:

```
netfree_018.crt
netfree_019.crt
netfree_099.crt
amitnet.crt
bezeq.crt
hadran_vpn.crt
hot.crt
ib_itc.crt
ib_partner.crt
ib_spotnet.crt
itc.crt
kosher_sim.crt
kosher_sim_cellcom.crt
ksim_itc.crt
ksim_partner.crt
ksim_pele.crt
netfree_anywhere.crt
sim_kasher_triple_c.crt
x2one.crt
yossi.crt
```

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
