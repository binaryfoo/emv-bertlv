package io.github.binaryfoo.crypto;

import io.github.binaryfoo.tlv.ISOUtil;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SignedDataRecovererTest {
  @Test
  public void testRecoverIssuerPublicKey() {
    String exepected = "6A0254133300121400000101019003A028E99BECB507C507243C2E8DF4FE56A0297CD0AE72E2CFA992A98C80788422DBE00A1395B1545B09D66CFAB9ECEAF413E3DFF8227BC80BF6DA7F142B32673C527BB79129B5965C0F5DC4C3732BE6FA284F2469CDC545CD8AF915D2DD4AF2E171F5D36D502C8F9498797B9DE111BF5EB97EF1820BA654E4867F09D6BB41BCB1E4FB3E9D287ABD670319181347312707BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB30BBB725EC32CEB6FF4A6334BB4F59CF048F2FBABC";
    byte[] signed = ISOUtil.hex2byte("7AF7C3297510E795704B4B34BB1526EB953FE7BEE08DBF08DFBA056AA42FC726BC03373A319DDC6197E2F5B333401AE3E4BBFC5044D245BDDEBEA146994132D939D72E8E05CC958856DFCB33B26427812955B09F7A7A68AAAB6B8326AD2929082569DA040BD286F2F20A759E62683995D4B840655342C8088FA18806C2290992665A45EC16AC6FE4B0FA664FC644B7B485CD4612B2967B846A9482DB7D16E9F9A3A8295CE5D2B91718180F1E86E67E8E2275EB2116E651BD91AAC49D80D6E64DEB54977258465C09274DD7D232A98CD80C76458CEAAA2B0A414EE7A1B8899E6361B098307EBE5E87F31C41B0A32B248CF6DA9849810C4955");
    byte[] modulus = ISOUtil.hex2byte("A191CB87473F29349B5D60A88B3EAEE0973AA6F1A082F358D849FDDFF9C091F899EDA9792CAF09EF28F5D22404B88A2293EEBBC1949C43BEA4D60CFD879A1539544E09E0F09F60F065B2BF2A13ECC705F3D468B9D33AE77AD9D3F19CA40F23DCF5EB7C04DC8F69EBA565B1EBCB4686CD274785530FF6F6E9EE43AA43FDB02CE00DAEC15C7B8FD6A9B394BABA419D3F6DC85E16569BE8E76989688EFEA2DF22FF7D35C043338DEAA982A02B866DE5328519EBBCD6F03CDD686673847F84DB651AB86C28CF1462562C577B853564A290C8556D818531268D25CC98A4CC6A0BDFFFDA2DCCA3A94C998559E307FDDF915006D9A987B07DDAEB3B");
    byte[] exponent = ISOUtil.hex2byte("03");
    byte[] recover = new SignedDataRecoverer().recover(signed, exponent, modulus);
    assertThat(ISOUtil.hexString(recover), is(exepected));
  }

  @Test
  public void testRecoverICCPublicKey() {
    String expected = "6A045413330089600044FFFF121400000101019003A028E99BECB507C507243C2E8DF4FE56A0297CD0AE72E2CFA992A98C80788422DBE00A1395B1545B09D66CFAB9ECEAF413E3DFF8227BC80BF6DA7F142B32673C527BB79129B5965C0F5DC4C3732BE6FA284F2469CDC545CD8AF915D2DD4AF2E171F5D36D502C42D0D7519B1CA8D3C689B65CC775687F051B2849BC";
    byte[] signed = ISOUtil.hex2byte("3826D61BE14E6691D03552DD9E7034150A3AFFB43AA8C13069483EF338AA272F0DB37354F4EB768B4373D365E3973B04486F43DB298E94F570EBBDD0BDF828360FCAD568D975838A9340ADAAF9974149436DE273AE996FD842FD93966D1CD58072B0D8C2D7F60D351B4C44791D82D0DEA591EC61DB5D0D32367086B6A879CB9289FD27D3B0C18E1474812407697F4116");
    byte[] modulus = ISOUtil.hex2byte("A028E99BECB507C507243C2E8DF4FE56A0297CD0AE72E2CFA992A98C80788422DBE00A1395B1545B09D66CFAB9ECEAF413E3DFF8227BC80BF6DA7F142B32673C527BB79129B5965C0F5DC4C3732BE6FA284F2469CDC545CD8AF915D2DD4AF2E171F5D36D502C8F9498797B9DE111BF5EB97EF1820BA654E4867F09D6BB41BCB1E4FB3E9D287ABD670319181347312707"); // issuer public key from tag 90
    byte[] exponent = ISOUtil.hex2byte("010001"); // from 9F32
    byte[] recover = new SignedDataRecoverer().recover(signed, exponent, modulus);
    assertThat(ISOUtil.hexString(recover), is(expected));
  }

  @Test
  public void testRecoverSignedDynamicApplicationData() {
    String expected = "6A0501260836DF6D9E2104092E40D58B731AF5885C067BE29D015DD4C9454026810F0879E219B8A7DCD0BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB5734B62BE6BFF2A04C1CFF4060E549C932E1723DBC";
    byte[] signed = ISOUtil.hex2byte("124CD68898FC0FF1B82E6033A63DF03F6601DED0405BE0DA6A557BA101AED95663C6EE32F895F6F2C7BBAC568E18643CAEB9762E6C24843B4998CCC8F1E608A61C9E1914315FE84D56350B10B3694AEECB8BC30ADA3DB4ECE331D5B7E76397BDBA7763148F535B9EBFBABE772AB88C7F302FA9E3010F5191B734E82594D08752EBB329D76FBBF901F779921CE81CFBDC");
    byte[] modulus = ISOUtil.hex2byte("A028E99BECB507C507243C2E8DF4FE56A0297CD0AE72E2CFA992A98C80788422DBE00A1395B1545B09D66CFAB9ECEAF413E3DFF8227BC80BF6DA7F142B32673C527BB79129B5965C0F5DC4C3732BE6FA284F2469CDC545CD8AF915D2DD4AF2E171F5D36D502C8F9498797B9DE111BF5EB97EF1820BA654E4867F09D6BB41BCB1E4FB3E9D287ABD670319181347312707"); // recovered from 9f46 + 9f48
    byte[] exponent = ISOUtil.hex2byte("010001"); // from 9F47
    byte[] recover = new SignedDataRecoverer().recover(signed, exponent, modulus);
    assertThat(ISOUtil.hexString(recover), is(expected));
  }
}
