The following are instructions to be used by Copilot when generating code. These instructions are designed to ensure that the code generated is correct, efficient, and maintainable.

2. For each file that needs changes:
   - Read the ENTIRE file first
   - Document the current functionality
   - Plan changes while preserving existing behavior
   - Make minimal necessary changes
   - Validate changes don't break existing functionality

3. Implementation Steps:
   - Start with core functionality changes
   - Test each change before moving to the next
   - Keep existing working code intact
   - Add new code rather than modifying working code where possible
   - Document any assumptions made

4. Code Review Steps:
   - Verify all changes are necessary
   - Check that existing functionality is preserved
   - Validate no unintended side effects
   - Test the complete feature flow

5. Error Prevention:
   - Never modify working code without understanding its purpose
   - Always keep code that already works
   - Make incremental changes
   - Check for errors after each change
   - If a change causes issues, revert immediately

Key Principles:
- Do no harm to existing code
- Make minimal necessary changes
- Understand before modifying
- Test after each change
- Revert problematic changes immediately
- Don't add mocks or stubs under any circumstances