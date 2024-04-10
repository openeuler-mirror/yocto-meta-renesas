LIC_FILES_CHKSUM = "file://LICENSE.md;md5=1fb5dca04b27614d6d04abca6f103d8d"
LICENSE="BSD-3-Clause"
PV = "1.06"

PACKAGE_ARCH = "${MACHINE_ARCH}"

#FLASH_WRITER_URL = "git://github.com/renesas-rz/rzg2_flash_writer"
#BRANCH = "rz_g2l"

#FLASH_WRITER_URL = "git://github.com/123markhong/yg2lx-flash_write.git"
#BRANCH = "yg2lx_fw"
#FLASH_WRITER_URL ="git://github.com/123markhong/myir-renesas_flash_writer.git"
#BRANCH = "rz_g2l"
FLASH_WRITER_URL ="git://github.com/MYiR-Dev/myir-renesas-flash-writer.git"
BRANCH = "develop-remi-v1.06"


# SRC_URI = "${FLASH_WRITER_URL};branch=${BRANCH}"
OPENEULER_LOCAL_NAME = "myir-renesas-flash-writer"
SRC_URI = " \
        file://myir-renesas-flash-writer \
"

SRCREV = "8cd973992da7cdc8646a498b81d1a1e4e9f74f20"

inherit deploy
#require include/provisioning.inc

S = "${WORKDIR}/myir-renesas-flash-writer"
PMIC_BUILD_DIR = "${S}/build_pmic"

do_compile() {
        BOARD="RZG2L_SMARC_PMIC_1GB";
        PMIC_BOARD="RZG2L_SMARC_PMIC";

        cd ${S}
	oe_runmake BOARD=${BOARD}

        if [ "${PMIC_SUPPORT}" = "1" ]; then
		oe_runmake OUTPUT_DIR=${PMIC_BUILD_DIR} clean;
		oe_runmake BOARD=${PMIC_BOARD} OUTPUT_DIR=${PMIC_BUILD_DIR};
	fi
}

do_install[noexec] = "1"

do_deploy() {
        install -d ${DEPLOYDIR}
        install -m 644 ${S}/AArch64_output/*.mot ${DEPLOYDIR}
        if [ "${PMIC_SUPPORT}" = "1" ]; then
        	install -m 644 ${PMIC_BUILD_DIR}/*.mot ${DEPLOYDIR}
	fi
}
PARALLEL_MAKE = "-j 1"
addtask deploy after do_compile
