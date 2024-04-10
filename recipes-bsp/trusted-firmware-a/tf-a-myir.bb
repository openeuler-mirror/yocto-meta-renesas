DESCRIPTION = "Trusted Firmware-A for RZ/G2H/M/N/E"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit deploy


OPENEULER_LOCAL_NAME = "myir-renesas-tf-a"
SRC_URI = " \
		file://myir-renesas-tf-a \
"

S = "${WORKDIR}/myir-renesas-tf-a"

PLATFORM ?= "rzg"

# For RZ/G2L Series

PLATFORM:myir-remi = "g2l"
#2G DDR
EXTRA_FLAGS:myir-remi = "BOARD=smarc_1"
PMIC_EXTRA_FLAGS:myir-remi = "BOARD=smarc_pmic_1"
FLASH_ADDRESS_BL2_BP:myir-remi = "00000"
FLASH_ADDRESS_FIP:myir-remi = "1D200"

PLATFORM_myir-remi-1g = "g2l"
EXTRA_FLAGS_myir-remi-1g = "BOARD=smarc_1"
PMIC_EXTRA_FLAGS_myir-remi-1g = "BOARD=smarc_pmic_1"
FLASH_ADDRESS_BL2_BP_myir-remi-1g = "00000"
FLASH_ADDRESS_FIP_myir-remi-1g = "1D200"

PLATFORM_myir-yg2lx = "g2l"
#2G DDR
EXTRA_FLAGS_myir-yg2lx = "BOARD=smarc_2"
PMIC_EXTRA_FLAGS_myir-yg2lx = "BOARD=smarc_pmic_2"
FLASH_ADDRESS_BL2_BP_myir-yg2lx = "00000"
FLASH_ADDRESS_FIP_myir-yg2lx = "1D200"

#1GDDR
PLATFORM_myir-yg2lx-1g = "g2l"
EXTRA_FLAGS_myir-yg2lx-1g = "BOARD=smarc_1"
PMIC_EXTRA_FLAGS_myir-yg2lx-1g = "BOARD=smarc_pmic_1"
FLASH_ADDRESS_BL2_BP_myir-yg2lx-1g = "00000"
FLASH_ADDRESS_FIP_myir-yg2lx-1g = "1D200"

#PLATFORM_smarc-rzg2l = "g2l"
#EXTRA_FLAGS_smarc-rzg2l = "BOARD=smarc_2"
#PMIC_EXTRA_FLAGS_smarc-rzg2l = "BOARD=smarc_pmic_2"
#FLASH_ADDRESS_BL2_BP_smarc-rzg2l = "00000"
#FLASH_ADDRESS_FIP_smarc-rzg2l = "1D200"

PLATFORM_smarc-rzg2lc = "g2l"
EXTRA_FLAGS_smarc-rzg2lc = "BOARD=smarc_1"
FLASH_ADDRESS_BL2_BP_smarc-rzg2lc = "00000"
FLASH_ADDRESS_FIP_smarc-rzg2lc = "1D200"

PLATFORM_smarc-rzg2ul = "g2ul"
EXTRA_FLAGS_smarc-rzg2ul = "BOARD=g2ul_smarc SOC_TYPE=1 SPI_FLASH=AT25QL128A"
FLASH_ADDRESS_BL2_BP_smarc-rzg2ul = "00000"
FLASH_ADDRESS_FIP_smarc-rzg2ul = "1D200"

PLATFORM_smarc-rzv2l = "v2l"
EXTRA_FLAGS_smarc-rzv2l = "BOARD=smarc_4"
PMIC_EXTRA_FLAGS_smarc-rzv2l = "BOARD=smarc_pmic_2"
FLASH_ADDRESS_BL2_BP_smarc-rzv2l = "00000"
FLASH_ADDRESS_FIP_smarc-rzv2l = "1D200"

PLATFORM_rzv2l-dev = "v2l"
EXTRA_FLAGS_rzv2l-dev = "BOARD=dev15_4"
FLASH_ADDRESS_BL2_BP_rzv2l-dev = "00000"
FLASH_ADDRESS_FIP_rzv2l-dev = "1D200"

PMIC_BUILD_DIR = "${S}/build_pmic"

# For RZ/G2H/M/N/E
ATFW_OPT_r8a774c0 = "LSI=G2E RCAR_SA0_SIZE=0 RCAR_DRAM_DDR3L_MEMCONF=1 RCAR_DRAM_DDR3L_MEMDUAL=1 SPD="none""
ATFW_OPT_r8a774a1 = "LSI=G2M RCAR_DRAM_SPLIT=2 SPD="none""
ATFW_OPT_r8a774b1 = "LSI=G2N SPD="none""
ATFW_OPT_r8a774e1 = "LSI=G2H RCAR_DRAM_SPLIT=2 RCAR_DRAM_LPDDR4_MEMCONF=1 RCAR_DRAM_CHANNEL=5 SPD="none""

LOSSY_ENABLE ?= "1"
ATFW_OPT_LOSSY = "${@oe.utils.conditional("LOSSY_ENABLE", "1", "RCAR_LOSSY_ENABLE=1", "", d)}"

ATFW_OPT:append:r8a774c0 = "${@oe.utils.conditional("USE_ECC", "1", " LIFEC_DBSC_PROTECT_ENABLE=0 RZG_DRAM_ECC=1 ", "",d)}"
ATFW_OPT:append:r8a774a1 = "${@oe.utils.conditional("USE_ECC", "1", " LIFEC_DBSC_PROTECT_ENABLE=0 RCAR_DRAM_SPLIT=0 RZG_DRAM_ECC=1 ", " ${ATFW_OPT_LOSSY} ",d)}"
ATFW_OPT:append:r8a774b1 = "${@oe.utils.conditional("USE_ECC", "1", " LIFEC_DBSC_PROTECT_ENABLE=0 RZG_DRAM_ECC=1 ", " ${ATFW_OPT_LOSSY} ",d)}"
ATFW_OPT:append:r8a774e1 = "${@oe.utils.conditional("USE_ECC", "1", " LIFEC_DBSC_PROTECT_ENABLE=0 RCAR_DRAM_SPLIT=0 RZG_DRAM_ECC=1 ", " ${ATFW_OPT_LOSSY} ",d)}"

ATFW_OPT:append = " RZG_DRAM_ECC_FULL=${ECC_FULL} "

ATFW_OPT:append = " RCAR_RPC_HYPERFLASH_LOCKED=0 MBEDTLS_DIR=../mbedtls "

# requires CROSS_COMPILE set by hand as there is no configure script
export CROSS_COMPILE="${TARGET_PREFIX}"

# Let the Makefile handle setting up the CFLAGS and LDFLAGS as it is a standalone application
CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

FILES:${PN}:myir-remi = "/boot "
SYSROOT_DIRS:myir-remi += "/boot"

export TF_LDFLAGS += " --no-warn-rwx-segments "

do_compile:myir-remi() {
# Build TF-A
    oe_runmake distclean
    oe_runmake PLAT=${PLATFORM} ${EXTRA_FLAGS} bl2 bl31

    if [ "${PMIC_SUPPORT}" = "1" ]; then
       oe_runmake PLAT=${PLATFORM} ${PMIC_EXTRA_FLAGS} BUILD_PLAT=${PMIC_BUILD_DIR} bl2 bl31
    fi
}

do_install:myir-remi () { 
    install -d ${D}/boot 
    install -m 644 ${S}/build/${PLATFORM}/release/bl2.bin ${D}/boot/bl2-${MACHINE}.bin
    install -m 644 ${S}/build/${PLATFORM}/release/bl31.bin ${D}/boot/bl31-${MACHINE}.bin

    if [ "${PMIC_SUPPORT}" = "1" ]; then
       install -m 0644 ${PMIC_BUILD_DIR}/bl2.bin ${D}/boot/bl2-${MACHINE}_pmic.bin
       install -m 0644 ${PMIC_BUILD_DIR}/bl31.bin ${D}/boot/bl31-${MACHINE}_pmic.bin
    fi
}

do_deploy() {
    # Create deploy folder
    install -d ${DEPLOYDIR}

    # Copy IPL to deploy folder
    install -m 0644 ${S}/build/${PLATFORM}/release/bl2/bl2.elf ${DEPLOYDIR}/bl2-${MACHINE}.elf
    install -m 0644 ${S}/build/${PLATFORM}/release/bl2.bin ${DEPLOYDIR}/bl2-${MACHINE}.bin
    install -m 0644 ${S}/build/${PLATFORM}/release/bl31/bl31.elf ${DEPLOYDIR}/bl31-${MACHINE}.elf
    install -m 0644 ${S}/build/${PLATFORM}/release/bl31.bin ${DEPLOYDIR}/bl31-${MACHINE}.bin
}

do_deploy:append:rzg2l() {
    if [ "${PMIC_SUPPORT}" = "1" ]; then
       install -m 0644 ${PMIC_BUILD_DIR}/bl2.bin ${DEPLOYDIR}/bl2-${MACHINE}_pmic.bin
       install -m 0644 ${PMIC_BUILD_DIR}/bl31.bin ${DEPLOYDIR}/bl31-${MACHINE}_pmic.bin
    fi
}

do_deploy:append:rzg2h() {
    install -m 0644 ${S}/build/${PLATFORM}/release/bl2.srec ${DEPLOYDIR}/bl2-${MACHINE}.srec
    install -m 0644 ${S}/build/${PLATFORM}/release/bl31.srec ${DEPLOYDIR}/bl31-${MACHINE}.srec
    install -m 0644 ${S}/tools/renesas/rzg_layout_create/bootparam_sa0.srec ${DEPLOYDIR}/bootparam_sa0.srec
    install -m 0644 ${S}/tools/renesas/rzg_layout_create/cert_header_sa6.srec ${DEPLOYDIR}/cert_header_sa6.srec
}
addtask deploy before do_build after do_compile
