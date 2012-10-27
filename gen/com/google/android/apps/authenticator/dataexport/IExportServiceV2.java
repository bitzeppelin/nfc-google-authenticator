/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/hansy/Proyectos/DevFestLima/NFC-Google-Authenticator/src/com/google/android/apps/authenticator/dataexport/IExportServiceV2.aidl
 */
package com.google.android.apps.authenticator.dataexport;
public interface IExportServiceV2 extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.google.android.apps.authenticator.dataexport.IExportServiceV2
{
private static final java.lang.String DESCRIPTOR = "com.google.android.apps.authenticator.dataexport.IExportServiceV2";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.google.android.apps.authenticator.dataexport.IExportServiceV2 interface,
 * generating a proxy if needed.
 */
public static com.google.android.apps.authenticator.dataexport.IExportServiceV2 asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.google.android.apps.authenticator.dataexport.IExportServiceV2))) {
return ((com.google.android.apps.authenticator.dataexport.IExportServiceV2)iin);
}
return new com.google.android.apps.authenticator.dataexport.IExportServiceV2.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getData:
{
data.enforceInterface(DESCRIPTOR);
android.os.Bundle _result = this.getData();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_onImportSucceeded:
{
data.enforceInterface(DESCRIPTOR);
this.onImportSucceeded();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.google.android.apps.authenticator.dataexport.IExportServiceV2
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public android.os.Bundle getData() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.os.Bundle _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getData, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = android.os.Bundle.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void onImportSucceeded() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onImportSucceeded, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onImportSucceeded = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public android.os.Bundle getData() throws android.os.RemoteException;
public void onImportSucceeded() throws android.os.RemoteException;
}
