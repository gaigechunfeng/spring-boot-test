@cd /d %~dp0
@set dcs_home=%cd%
@if exist "target" rd /s /q "target"
@call mvn package -Dmaven.test.skip=true

@if exist "dist" rd /s /q "dist"
@set dcs_build_dist="%dcs_home%\dist\spring-boot-test"
@mkdir %dcs_build_dist%
@move target\spring-boot-test-1.0.1.jar %dcs_build_dist%
@copy bin\startup.bat %dcs_build_dist%
@pause