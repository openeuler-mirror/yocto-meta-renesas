DESCRIPTION = "Linux kernel for the RZG2 based board"

RDEPENDS:${KERNEL_PACKAGE_NAME}-image:remove = "${@oe.utils.conditional('KERNEL_IMAGETYPE', 'vmlinux', '${KERNEL_PACKAGE_NAME}-vmlinux (= ${EXTENDPKGV})', '', d)}"

require recipes-kernel/linux/linux-yocto.inc
# require include/docker-control.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}/:"

OPENEULER_LOCAL_NAME = "myir-renesas-linux"
SRC_URI += "\
        file://myir-renesas-linux \
"

LINUX_VERSION = "5.10.83"

SRC_URI += " \
    file://0001-arm64-dts-renesas-rzg2l-smarc-Add-uio-support.patch \
    file://0002-arm64-dts-renesas-rzg2l-smarc-Disable-OSTM2.patch \
    file://0003-arm64-dts-renesas-rzg2lc-smarc-Add-uio-support.patch \
    file://0004-arm64-dts-renesas-rzg2ul-smarc-Add-uio-support.patch \
    file://0005-arm64-dts-renesas-rzg2lc-smarc-Disable-SCIF1-OSTM2.patch \
    file://0006-clk-renesas-r9a07g044-Set-SCIF1-SCIF2-OSTM2.patch \
    file://0007-arm64-dts-renesas-rzg2ul-smarc-Disable-OSTM2.patch \
    file://0008-clk-renesas-r9a07g043-Set-OSTM2.patch \
"
# Kernel confguration update
SRC_URI += "file://uio.cfg"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

PV = "${LINUX_VERSION}"
PR = "r1"

S = "${WORKDIR}/myir-renesas-linux"

SRC_URI:append = "\
  ${@oe.utils.conditional("USE_DOCKER", "1", " file://docker.cfg ", "", d)} \
  file://touch.cfg \
"

KBUILD_DEFCONFIG = "mys_g2lx_defconfig"
KCONFIG_MODE = "alldefconfig"

do_kernel_metadata_af_patch() {
	# need to recall do_kernel_metadata after do_patch for some patches applied to defconfig
	rm -f ${WORKDIR}/defconfig
	do_kernel_metadata
}

do_deploy:append() {
	for dtbf in ${KERNEL_DEVICETREE}; do
		dtb=`normalize_dtb "$dtbf"`
		dtb_ext=${dtb##*.}
		dtb_base_name=`basename $dtb .$dtb_ext`
		for type in ${KERNEL_IMAGETYPE_FOR_MAKE}; do
			ln -sf $dtb_base_name-${KERNEL_DTB_NAME}.$dtb_ext $deployDir/$type-$dtb_base_name.$dtb_ext
		done
	done
}

addtask do_kernel_metadata_af_patch after do_patch before do_kernel_configme

# Fix race condition, which can causes configs in defconfig file be ignored
do_kernel_configme[depends] += "virtual/${TARGET_PREFIX}binutils:do_populate_sysroot"
do_kernel_configme[depends] += "virtual/${TARGET_PREFIX}gcc:do_populate_sysroot"
do_kernel_configme[depends] += "bc-native:do_populate_sysroot bison-native:do_populate_sysroot"

# Fix error: openssl/bio.h: No such file or directory
DEPENDS += "openssl-native"
